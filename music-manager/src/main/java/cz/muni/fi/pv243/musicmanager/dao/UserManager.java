package cz.muni.fi.pv243.musicmanager.dao;

import javax.ejb.Local;

import cz.muni.fi.pv243.musicmanager.entities.User;
@Local
public interface UserManager {
	public User createUser(User user);

}
