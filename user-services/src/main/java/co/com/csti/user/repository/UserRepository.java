package co.com.csti.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.com.csti.user.model.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

}
