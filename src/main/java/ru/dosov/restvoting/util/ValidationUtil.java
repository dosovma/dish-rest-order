package ru.dosov.restvoting.util;

import ru.dosov.restvoting.model.AbstractEntity.HasId;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.util.exception.ForbiddenException;
import ru.dosov.restvoting.util.exception.IllegalRequestDataException;
import ru.dosov.restvoting.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ValidationUtil {

    public static void checkNew(HasId<Integer> entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id = null)");
        }
    }

    public static void checkPermission(AuthUser authUser, Integer candidate_id) {
        User user = authUser.getUser();
        if (!user.getId().equals(candidate_id)) {
            if (!user.getRoles().contains(Role.ADMIN)) {
                throw new ForbiddenException("User id=" + user.getId() + " can't read, update or delete user with id = " + candidate_id);
            }
        }
    }

    public static void assureIdConsistent(HasId<Integer> entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be with id = " + id);
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

    public static void checkVoteTime(LocalDateTime voteDateTime, LocalTime deadLine) {
        if (voteDateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new ForbiddenException("You can't change or delete your old vote");
        }
        if (voteDateTime.toLocalTime().isAfter(deadLine)) {
            throw new ForbiddenException("You can't change or delete your vote after " + deadLine);
        }
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(HttpServletRequest req, Exception e) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
/*        if (logStackTrace) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }*/
        return rootCause;
    }
}
