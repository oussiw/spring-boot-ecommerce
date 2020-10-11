package com.ostros.springbootecommerce.config;

import com.ostros.springbootecommerce.entity.Country;
import com.ostros.springbootecommerce.entity.Product;
import com.ostros.springbootecommerce.entity.ProductCategory;
import com.ostros.springbootecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//This class is used to restrict the exposure of some HttpMethods to the user
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;

    //    @Autowired
//    public MyDataRestConfig(EntityManager theEntityManager){
//        entityManager = theEntityManager;
//    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        //disable this methods for Product.class
        disableHttpMethods(Product.class, config, theUnsupportedActions);

        //disable this methods for ProductCategory.class
        disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);

        //disable this methods for Country.class
        disableHttpMethods(Country.class, config, theUnsupportedActions);

        //disable this methods for Country.class
        disableHttpMethods(State.class, config, theUnsupportedActions);

        // call an internal helper method to expose the ids
        exposeIds(config);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // this is used to expose entity ids

        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();

        for (EntityType entityType : entities) {
            entityClasses.add(entityType.getJavaType());
        }

        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
