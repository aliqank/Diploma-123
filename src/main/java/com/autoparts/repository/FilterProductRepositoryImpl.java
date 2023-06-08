package com.autoparts.repository;

import com.autoparts.dto.filter.ProductFilter;
import com.autoparts.dto.filter.SearchFilter;
import com.autoparts.entity.*;
import com.autoparts.entity.order.QOrderItem;
import com.autoparts.qerydsl.QPredicates;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.autoparts.entity.QBrand.brand;
import static com.autoparts.entity.QCategory.category;
import static com.autoparts.entity.QTovar.tovar;


@RequiredArgsConstructor
public class FilterProductRepositoryImpl implements FilterProductRepository{

    private final EntityManager entityManager;

    @Override
    public List<Tovar> findAllByFilter(ProductFilter filter) {

        var predicates = QPredicates.builder()
                .add(filter.min() != null ? filter.min() : Integer.MIN_VALUE, tovar.price::goe)
                .add(filter.max() != null ? filter.max() : Integer.MAX_VALUE, tovar.price::loe)
                .add(filter.rating(),  tovar.rating::in)
                .add(filter.brand(),tovar.brand.slug::containsIgnoreCase)
                .add(filter.category(), tovar.category.slug::containsIgnoreCase)
                .build();

        return  new JPAQuery<Tovar>(entityManager)
                .select(tovar)
                .from(tovar)
                .where(predicates)
                .fetch();

    }





    @Override
    public List<Tovar> searchByName(SearchFilter filter) {

        var predicates = QPredicates.builder()
                .add(filter.name(), tovar.name::containsIgnoreCase)
                .build();

        return new JPAQuery<Tovar>(entityManager)
                .select(tovar)
                .from(tovar)
                .where(predicates)
                .fetch();
    }

    @Override
    public List<Category> searchByCategoryName(SearchFilter filter) {

        var predicates = QPredicates.builder()
                .add(filter.name(), category.name::containsIgnoreCase)
                .build();

        return new JPAQuery<Tovar>(entityManager)
                .select(category)
                .from(category)
                .where(predicates)
                .fetch();
    }

    @Override
    public List<Tovar> findAllDsl(int size) {

        return new JPAQuery<Tovar>(entityManager)
                .select(tovar)
                .from(tovar)
                .fetchAll()
                .fetch();
    }

    public List<String> findPopularBrandNames() {

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        List<Tuple> results = query.select(brand.name, tovar.count())
                .from(tovar)
                .join(tovar.brand, brand)
                .groupBy(brand.name)
                .orderBy(tovar.count().desc())
                .limit(5)
                .fetch();

        return results.stream()
                .map(tuple -> tuple.get(brand.name))
                .collect(Collectors.toList());

    }


    public List<String> findPopularCategoryNames() {

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        List<Tuple> results = query.select(category.name, tovar.count())
                .from(tovar)
                .join(tovar.category, category)
                .groupBy(category.name)
                .orderBy(tovar.count().desc())
                .limit(5)
                .fetch();

        return results.stream()
                .map(tuple -> tuple.get(category.name))
                .collect(Collectors.toList());

    }

    public List<Tovar> findSpecialProducts(Pageable pageable) {

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);

        BooleanExpression compareAtPriceIsNotNull = tovar.compareAtPrice.isNotNull();

        OrderSpecifier<Long> orderByCompareAtPriceDesc = tovar.compareAtPrice.desc().nullsLast();
        OrderSpecifier<Integer> orderByPriceDesc = tovar.price.desc();

        return query
                .select(tovar)
                .from(tovar)
                .where(compareAtPriceIsNotNull)
                .orderBy(orderByCompareAtPriceDesc, orderByPriceDesc)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    public List<String> findProductNamesByIdIN(List<Long> ids){

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);

        return query.select(tovar.name)
                .from(tovar)
                .where(tovar.id.in(ids))
                .fetch();
    }


    public List<Tovar> findPopularProductsByBrand(String brandName, int limit) {
        QTovar tovar = QTovar.tovar;
        QCart cart = QCart.cart;
        QWishlist wishlist = QWishlist.wishlist;
        QBrand brand = QBrand.brand;

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(tovar)
                .from(tovar)
                .leftJoin(tovar.brand, brand)
                .leftJoin(cart).on(cart.productId.eq(tovar.id))
                .leftJoin(wishlist).on(wishlist.productId.eq(tovar.id))
                .where(brand.name.eq(brandName))
                .groupBy(tovar.id)
                .orderBy(tovar.id.count().desc())
                .limit(limit)
                .fetch();

    }

    public List<Tovar> findPopularProductsByCategory(String name, int limit) {
        QTovar tovar = QTovar.tovar;
        QCart cart = QCart.cart;
        QWishlist wishlist = QWishlist.wishlist;
        QCategory category = QCategory.category;

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(tovar)
                .from(tovar)
                .leftJoin(tovar.category, category)
                .leftJoin(cart).on(cart.productId.eq(tovar.id))
                .leftJoin(wishlist).on(wishlist.productId.eq(tovar.id))
                .where(category.name.eq(name))
                .groupBy(tovar.id)
                .orderBy(tovar.id.count().desc())
                .limit(limit)
                .fetch();

    }

    public List<Tovar> findPopularProducts(int limit) {
        QTovar tovar = QTovar.tovar;
        QCart cart = QCart.cart;
        QWishlist wishlist = QWishlist.wishlist;
        QCategory category = QCategory.category;

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(tovar)
                .from(tovar)
                .leftJoin(cart).on(cart.productId.eq(tovar.id))
                .leftJoin(wishlist).on(wishlist.productId.eq(tovar.id))
                .groupBy(tovar.id)
                .orderBy(tovar.id.count().desc())
                .limit(limit)
                .fetch();

    }

    public List<Tovar> findMostSoldProducts(int limit) {
        QTovar tovar = QTovar.tovar;
        QOrderItem orderItem = QOrderItem.orderItem;

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);

        return query.select(tovar)
                .from(tovar)
                .leftJoin(orderItem).on(orderItem.tovar.id.eq(tovar.id))
                .fetchJoin()
                .groupBy(tovar.id)
                .orderBy(orderItem.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }

    //    public List<Tovar> getTopRatedProducts(String categorySlug, int limit) {
//
//        var predicates = QPredicates.builder()
//                .add()
//                .build();
//
//        return new JPAQuery<Tovar>(entityManager)
//                .select(tovar)
//                .from(tovar)
//                .where(predicates)
//                .fetch();
//    }


}
