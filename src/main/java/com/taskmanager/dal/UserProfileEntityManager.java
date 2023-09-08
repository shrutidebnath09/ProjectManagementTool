package com.taskmanager.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.taskmanager.models.UserProfile;

/**
 * An entity manager for user profiles
 * 
 */
public class UserProfileEntityManager {

	private final EntityManager entityManager;

	public UserProfileEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void create(UserProfile userProfile) {
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(userProfile);
		this.entityManager.getTransaction().commit();
	}

	public void edit(UserProfile userProfile) {
		this.entityManager.getTransaction().begin();
		this.entityManager.merge(userProfile);
		this.entityManager.getTransaction().commit();
	}

	public void remove(UserProfile userProfile) {
		this.entityManager.getTransaction().begin();
		this.entityManager.remove(userProfile);
		this.entityManager.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public UserProfile findById(long id) {
		final Query query = this.entityManager.createQuery(
				"select o from UserProfile o where o.id = :id").setParameter(
				"id", id);

		final List<UserProfile> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public UserProfile findByUsername(final String username) {
		final Query query = this.entityManager.createQuery(
				"select o from UserProfile o where o.username = :username")
				.setParameter("username", username);

		final List<UserProfile> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

	/**
	 * Finds a user profile by email
	 * 
	 * @param email
	 * @return {@link UserProfile} the found profile or null
	 */
	@SuppressWarnings("unchecked")
	public UserProfile findByEmail(final String email) {
		final Query query = this.entityManager.createQuery(
				"select o from UserProfile o where o.email = :email")
				.setParameter("email", email);

		final List<UserProfile> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

}
