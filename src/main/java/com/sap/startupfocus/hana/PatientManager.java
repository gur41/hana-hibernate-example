package com.sap.startupfocus.hana;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class PatientManager {
	private static SessionFactory factory;
	private final static Logger log = LogManager.getLogger(PatientManager.class);
	   
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().addAnnotatedClass(Patient.class).buildSessionFactory();
		} catch (Exception ex) {
			log.error("Failed to create sessionFactory object.", ex);
			System.exit (-1);
		}
	      
		PatientManager pManager = new PatientManager ();

		/* Add a few records in database */
		Integer patientId1 = pManager.addPatient(101, "Jack", "Wolf");
		Integer patientId2 = pManager.addPatient(102, "Deepak", "Das");
		Integer patientId3 = pManager.addPatient(103, "John", "Jones");

		/* List all patients */

		
		factory.close();
	}
	   
	/* Method to create a patient in the database */
	public Integer addPatient (Integer patientId, String fname, String lname) {
		Session session = factory.openSession();
	      
		Patient patient = new Patient();
		patient.setFirstName(fname);
		patient.setLastName(lname);
		patient.setId(patientId.intValue());
	      
		Transaction txn = session.beginTransaction();
		try {
			Object o = session.save(patient);
			log.debug("Value of save return = {}", o);
			txn.commit();
		} catch (HibernateException e) {
			log.error("Exception received while adding patient.", e);
			if (txn != null) txn.rollback();
			return -1;
	    } finally {
	         session.close();
	    }
	      
		return patientId;
	}
	   
	/* Method to list all patients */

}
