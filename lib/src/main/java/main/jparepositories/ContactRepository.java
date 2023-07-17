package main.jparepositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.dbmodels.DbContact;

@Repository
public interface ContactRepository extends JpaRepository<DbContact, Long>{
	public List<DbContact> findByUserId(Long userId);
	public DbContact findById(Long id);
	public Page<DbContact> findByUserId(Long userId, Pageable pageable);
}
