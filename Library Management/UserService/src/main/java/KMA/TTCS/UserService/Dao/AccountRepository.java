package KMA.TTCS.UserService.Dao;

import KMA.TTCS.UserService.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "account")
public interface AccountRepository extends JpaRepository<Account , Integer> {
    public Account findAccountByUsername(String username);
    public Account findAccountById(int id);
}
