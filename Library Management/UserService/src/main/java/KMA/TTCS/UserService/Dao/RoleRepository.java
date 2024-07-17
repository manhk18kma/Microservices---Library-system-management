package KMA.TTCS.UserService.Dao;

import KMA.TTCS.UserService.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "role")
public interface RoleRepository extends JpaRepository<Role , Integer> {
}
