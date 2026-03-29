package at.ac.htlinn.hamsterbackend.role;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.htlinn.hamsterbackend.role.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}