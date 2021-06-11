package com.candlebe.gcoach.repository.search;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.QContent;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchContentRepositoryImpl extends QuerydslRepositorySupport implements SearchContentRepository {

    QContent content = QContent.content1;

    public SearchContentRepositoryImpl() {
        super(Content.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage....................");
        log.info("category : " + type + " | keyword : " + keyword);

        JPQLQuery<Content> jpqlQuery = from(content);
        JPQLQuery<Tuple> tuple = jpqlQuery.select(
                content.cid,
                content.title,
                content.content,
                content.category1,
                content.category2,
                content.category3
        );
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // where 문에 들어가는 조건을 넣어주는 컨테이너
        BooleanExpression expression = content.cid.gt(0L); // bno > 0 조건만 생성

        booleanBuilder.and(expression);

        if (type != null) {
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            conditionBuilder.or(content.category1.contains(type));
            conditionBuilder.or(content.category2.contains(type));
            conditionBuilder.or(content.category3.contains(type));
            conditionBuilder.and(content.title.contains(keyword));
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        // Order by
        Sort sort = pageable.getSort();

        // tuple.orderBy(board.bno.desc()); // 직접 코드로 처리하면

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Content.class, "content1");
            jpqlQuery.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        /* ----- */

        // Page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());
        /* ----- */

        List<Tuple> result = tuple.fetch(); // 조회 대상이 여러건일 경우 컬렉션 반환
        log.info(result);

        long count = jpqlQuery.fetchCount();
        log.info("COUNT : " + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count
        );
    }
}
