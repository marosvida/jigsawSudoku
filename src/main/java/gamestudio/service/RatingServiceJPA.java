package gamestudio.service;

import gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            getRating(rating.getGame(), rating.getPlayer());
            entityManager.createNamedQuery("Rating.updateRating")
                    .setParameter("rating", rating.getRating())
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .executeUpdate();
        } catch (RatingException e){
            entityManager.persist(rating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Object o = entityManager.createNamedQuery("Rating.getAverageRating")
                    .setParameter("game", game)
                    .getSingleResult();
            if(o != null){
                return (int) Math.round((Double) o);
            } else {
                return 0;
            }
        } catch (NoResultException e){
            throw new RatingException("There is no rating in database.", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            return (int) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (NoResultException e){
            throw new RatingException("No such a rating in database.", e);
        }

    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}
