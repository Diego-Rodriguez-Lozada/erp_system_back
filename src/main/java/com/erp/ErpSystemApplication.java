package com.erp;

import com.erp.auth.domain.RequestContext;
import com.erp.auth.entity.RoleEntity;
import com.erp.auth.enumeration.Authority;
import com.erp.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class ErpSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			RequestContext.setUserId(0L);

			var superAdminRole = new RoleEntity();
			superAdminRole.setName(Authority.SUPER_ADMIN.name());
			superAdminRole.setAuthorities(Authority.SUPER_ADMIN);
			roleRepository.save(superAdminRole);

			var adminRole = new RoleEntity();
			adminRole.setName(Authority.ADMIN.name());
			adminRole.setAuthorities(Authority.ADMIN);
			roleRepository.save(adminRole);

			var managerRole = new RoleEntity();
			managerRole.setName(Authority.MANAGER.name());
			managerRole.setAuthorities(Authority.MANAGER);
			roleRepository.save(managerRole);

			var salesRole = new RoleEntity();
			salesRole.setName(Authority.SALES.name());
			salesRole.setAuthorities(Authority.SALES);
			roleRepository.save(salesRole);

			var purchasingRole = new RoleEntity();
			purchasingRole.setName(Authority.PURCHASING.name());
			purchasingRole.setAuthorities(Authority.PURCHASING);
			roleRepository.save(purchasingRole);

			var accountantRole = new RoleEntity();
			accountantRole.setName(Authority.ACCOUNTANT.name());
			accountantRole.setAuthorities(Authority.ACCOUNTANT);
			roleRepository.save(accountantRole);

			var warehouseRole = new RoleEntity();
			warehouseRole.setName(Authority.WAREHOUSE.name());
			warehouseRole.setAuthorities(Authority.WAREHOUSE);
			roleRepository.save(warehouseRole);

			var userRole = new RoleEntity();
			userRole.setName(Authority.USER.name());
			userRole.setAuthorities(Authority.USER);
			roleRepository.save(userRole);

		};
	}
}
