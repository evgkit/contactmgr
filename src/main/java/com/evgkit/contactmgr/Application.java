package com.evgkit.contactmgr;

import com.evgkit.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("Dude", "Some")
                .withEmail("example@example.com")
                .build();

        save(contact);

        for (Contact fetchedContact : fetchAllContacts()) {
            System.out.println(fetchedContact);
        }
    }

    private static void save(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(contact);

        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();

        session.close();

        return contacts;
    }
}
