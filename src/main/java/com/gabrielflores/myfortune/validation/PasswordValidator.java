package com.gabrielflores.myfortune.validation;

import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.exception.InvalidTokenException;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.password.PasswordChecker;
import com.gabrielflores.myfortune.password.PasswordResult;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.service.user.UsuarioService;
import com.gabrielflores.myfortune.validation.Password.Action;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
@Component
public class PasswordValidator implements ConstraintValidator<Password, Object> {

    private Action action;
    private String passwordField;

    private final PasswordChecker passwordChecker;
    private final UsuarioService usuarioService;

    public PasswordValidator(PasswordChecker passwordChecker, UsuarioService usuarioService) {
        this.passwordChecker = passwordChecker;
        this.usuarioService = usuarioService;
    }

    @Override
    public void initialize(Password constraintAnnotation) {
        this.action = constraintAnnotation.action();
        this.passwordField = constraintAnnotation.passwordField();
    }

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> errors = new LinkedList<>();
        try {
            String password = BeanUtils.getProperty(objectToValidate, passwordField);
            Map<String, Object> parametros;
            switch (action) {
                case UPDATE -> {
                    String id = BeanUtils.getProperty(objectToValidate, "id");
                    if (StringUtils.isBlank(id)) {
                        context.buildConstraintViolationWithTemplate("Usuário não encontrado")
                                .addPropertyNode(passwordField).addConstraintViolation();
                        return false;
                    }
                    final Usuario usuario = usuarioService.getById(Long.valueOf(id));
                    parametros = getParametros(usuario);
                }
                case RESET -> {
                    String token = BeanUtils.getProperty(objectToValidate, "token");
                    if (StringUtils.isBlank(token)) {
                        context.buildConstraintViolationWithTemplate("Token não encontrado")
                                .addPropertyNode(passwordField).addConstraintViolation();
                        return false;
                    }
                    final Usuario usuario = usuarioService.findByToken(token);
                    parametros = getParametros(usuario);
                }
                case INSERT -> {
                    if (StringUtils.isBlank(password)) {
                        return true; //opcional no insert
                    }
                    parametros = PropertyUtils.describe(objectToValidate);
                }
                default -> {
                    throw new UnsupportedOperationException("Tipo não suportado");
                }
            }
            errors.addAll(checkPassword(password, parametros));
        } catch (UnsupportedOperationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InvalidTokenException | EntityNotFoundException e) {
            context.buildConstraintViolationWithTemplate(e.getLocalizedMessage())
                    .addPropertyNode(passwordField).addConstraintViolation();
            return false;
        }
        if (!errors.isEmpty()) {
            errors.forEach(error -> context
                    .buildConstraintViolationWithTemplate(error)
                    .addPropertyNode(passwordField)
                    .addConstraintViolation());
        }
        return errors.isEmpty();
    }

    private List<String> checkPassword(String senha, Map<String, Object> rulesParams) {
        final Map<String, PasswordResult> resultado = passwordChecker.evaluate(senha, rulesParams);
        return resultado.values().stream()
                .filter(r -> r.isNotValid() && r.isParcial())
                .map(PasswordResult::getRule)
                .filter(PasswordValidationRule.class::isInstance)
                .map(PasswordValidationRule.class::cast)
                .map(PasswordValidationRule::getValidationError)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getParametros(final Usuario usuario) {
        return Map.of(
                "cpf", usuario.getCpf(),
                "email", usuario.getEmail(),
                "nome", usuario.getNome(),
                "senha", usuario.getSenha()
        );
    }

}
