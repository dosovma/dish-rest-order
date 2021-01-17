package ru.dosov.restvoting.util;

import org.springframework.validation.BindingResult;
import ru.dosov.restvoting.model.HasId;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.util.exception.ForbiddenException;
import ru.dosov.restvoting.util.exception.IllegalRequestDataException;
import ru.dosov.restvoting.util.exception.NotFoundException;
import ru.dosov.restvoting.web.AuthUser;

import java.util.stream.Collectors;

public class ValidationUtil {

    public static void checkNew(HasId<Integer> entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity + " must be new (id = null)");
        }
    }

    public static void checkPermission(AuthUser authUser, Integer candidate_id) {
        User user = authUser.getUser();
        if (!user.getId().equals(candidate_id)) {
            if (!user.getRoles().contains(Role.ADMIN)) {
                throw new ForbiddenException(user + " can't read, update or delete user with id = " + candidate_id);
            }
        }
    }

    public static void assureIdConsistent(HasId<Integer> entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalRequestDataException(entity + " must be with id = " + id);
        }
    }

    public static <T> T checkNotFound(T object, int id) {
        if (object == null) {
            throw new NotFoundException("Not found entity with id = " + id);
        }
        return object;
    }

    public static void checkNotFound(Boolean found, int id) {
        if (!found) {
            throw new NotFoundException("Not found entity with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
    }
}
