package gamestudio.service;

import gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserServiceJPA implements UserService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public boolean isUser(String login) {
        try{
            entityManager.createNamedQuery("User.getPassword")
                    .setParameter("login", login).getSingleResult();
        } catch (NoResultException e){
            return false;
        }
        return true;
    }

    @Override
    public String getPassword(String login) {
        return (String) entityManager.createNamedQuery("User.getPassword")
                .setParameter("login", login).getSingleResult();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}
