package common.validator;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.Validation;

/**
 * This class contains a single class-level (static) method checking for Bean Validation constraint annotation violations.
 * <p>
 * The following example shows to validate a Region object named newRegion.
 * <p>
 * String errorMessage = BeanValidator.validateBean(Region.class, newRegion);
 * if (errorMessage != null) {
 * return Response
 * .status(Response.Status.BAD_REQUEST)
 * .entity(errorMessage)
 * .build();
 * }
 *
 * @version 2023.11.05
 */
public class BeanValidator {

    /**
     * @param typeInstance The object with bean validation constraint annotations to validate.
     * @param <T>          This is a generic method that can operator on a type specified by the caller.
     * @return A JSON object with the property name and message for each validation error message.
     */
    public static <T> String validateBean(T typeInstance) {
        String errorMessage = null;

        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            var constraintViolations = validatorFactory.getValidator().validate(typeInstance);
            if (!constraintViolations.isEmpty()) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                for (var singleConstraintViolation : constraintViolations) {
                    jsonObjectBuilder.add(singleConstraintViolation.getPropertyPath().toString(), singleConstraintViolation.getMessage());
                }
                errorMessage = jsonObjectBuilder.build().toString();
            }
        }

        return errorMessage;
    }
}