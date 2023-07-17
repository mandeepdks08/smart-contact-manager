package main.jparepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.dbmodels.DbUser;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {
	@SuppressWarnings("unchecked")
	public DbUser save(DbUser dbUser);
	public Optional<DbUser> findById(Long id);
	public DbUser findByEmail(String email);
}
