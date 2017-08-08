package com.evgkit.contactmgr;

import com.evgkit.contactmgr.model.Contact;
import com.github.javafaker.Faker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Faker faker = new Faker();

        Contact contact = new Contact.ContactBuilder(faker.firstName(), faker.lastName())
                .withEmail("example@example.com")
                .build();

        save(contact);

        fetchAllContacts().forEach(System.out::println);
    }

    private static int save(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        int id = (int)session.save(contact);

        session.getTransaction().commit();
        session.close();

        return id;
    }

    private static void delete(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(contact);

        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        criteriaQuery.from(Contact.class);
        List<Contact> contacts = session.createQuery(criteriaQuery).getResultList();

        session.close();

        return contacts;
    }

    private static Contact findContactById(int id) {
        Session session = sessionFactory.openSession();

        Contact contact = session.get(Contact.class, id);

        session.close();

        return contact;
    }

    private static void updateContact(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(contact);

        session.getTransaction().commit();
        session.close();
    }
}
