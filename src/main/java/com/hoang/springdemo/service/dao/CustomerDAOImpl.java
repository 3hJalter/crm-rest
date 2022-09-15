package com.hoang.springdemo.service.dao;

import com.hoang.springdemo.service.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveCustomer(Customer customer) {
        // get current hibernate session
        Session session = sessionFactory.getCurrentSession();
        // save the customer
        session.saveOrUpdate(customer);
    }

    @Override
    public void deleteCustomer(int id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Customer customer = currentSession.get(Customer.class, id);
        currentSession.delete(customer);
    }

    @Override
    public List<Customer> getCustomers() {
        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // create a query that get and sort customers by lastName
        Query<Customer> query =
                currentSession.createQuery("FROM Customer ORDER BY lastName", Customer.class);

        // return the results

        return query.getResultList();
    }

    @Override
    public Customer getCustomer(int id) {
        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();
        // retrieve from database using primary key
        Customer customer = currentSession.get(Customer.class, id);
        return customer;
    }

    @Override
    public List<Customer> searchCustomers(String searchName) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = null;
        // Only search if "searchName" is not empty
        if (searchName != null && searchName.trim().length() > 0) {
            // search for firstName or lastName ... case insensitive
            query = currentSession.createQuery("from Customer where lower(firstName) like :theName " +
                    "or lower(lastName) like :theName ORDER BY lastName", Customer.class);
            query.setParameter("theName", "%" + searchName.toLowerCase() + "%");
        }
        else {
            // searchName is empty ... so just get all customers
            query =currentSession.createQuery("from Customer ORDER BY lastName", Customer.class);
        }
        // execute query and get result list
        List<Customer> customers = query.getResultList();
        // return the results
        return customers;
    }
}
