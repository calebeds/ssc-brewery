package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PreAuthorize("hasAuthority('customer.create')")
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerCreatePermission {
}
