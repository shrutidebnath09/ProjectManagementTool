package com.taskmanager.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.taskmanager.models.Task;

/**
 * An entity manager for tasks
 * 
 */
public class TaskEntityManager {

	private final EntityManager entityManager;

	public TaskEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void create(Task task) {
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(task);
		this.entityManager.getTransaction().commit();
	}

	public Task edit(Task task) {
		this.entityManager.getTransaction().begin();
		final Task updatedTask = this.entityManager.merge(task);
		this.entityManager.getTransaction().commit();
		return updatedTask;
	}

	public void remove(Task task) {
		this.entityManager.getTransaction().begin();
		this.entityManager.remove(task);
		this.entityManager.getTransaction().commit();
	}

	public int deleteByOwner(long owner) {
		final Query query = this.entityManager.createQuery(
				"delete from Task o where o.owner = :owner").setParameter(
				"owner", owner);
		this.entityManager.getTransaction().begin();
		int result = query.executeUpdate();
		this.entityManager.getTransaction().commit();
		return result;
	}

	public int deleteByIdAndOwner(long id, long owner) {
		final Query query = this.entityManager
				.createQuery(
						"delete from Task o where o.id = :id and o.owner = :owner")
				.setParameter("id", id).setParameter("owner", owner);
		this.entityManager.getTransaction().begin();
		int result = query.executeUpdate();
		this.entityManager.getTransaction().commit();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Task findById(long id) {
		final Query query = this.entityManager.createQuery(
				"select o from Task o where o.id = :id").setParameter("id", id);

		final List<Task> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Task> findByOwner(long owner) {
		final Query query = this.entityManager.createQuery(
				"select o from Task o where o.owner = :owner").setParameter(
				"owner", owner);

		final List<Task> resultList = query.getResultList();

		return resultList;
	}
}
