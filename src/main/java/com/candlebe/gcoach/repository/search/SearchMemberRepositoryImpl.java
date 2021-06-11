package com.candlebe.gcoach.repository.search;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.QMember;
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
public class SearchMemberRepositoryImpl extends QuerydslRepositorySupport implements SearchMemberRepository {
    public SearchMemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage....................");

        // 동적으로 처리하기 위해 Q 도매인 클래스 가져오기
        QMember member = QMember.member;

        JPQLQuery<Member> jpqlQuery = from(member).select(member);
        JPQLQuery<Tuple> tuple = jpqlQuery.select(
                member.mid,
                member.username,
                member.name,
                member.nickname,
                member.phone,
                member.emotion,
                member.interest,
                member.socialType);
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // where 문에 들어가는 조건을 넣어주는 컨테이너
        BooleanExpression expression = member.mid.gt(0L);

        booleanBuilder.and(expression);

        if (type != null) {
            String[] typeArr = type.split(""); // 문자열을 나눠 배열로 저장
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for (String t : typeArr) {
                switch (t) {
                    case "i":
                        conditionBuilder.or(member.username.contains(keyword));
                        break;
                    case "n":
                        conditionBuilder.or(member.name.contains(keyword));
                        break;
                    case "ni":
                        conditionBuilder.or(member.nickname.contains(keyword));
                        break;
                    case "p":
                        conditionBuilder.or(member.phone.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        // Order by
        Sort sort = pageable.getSort();

        // tuple.orderBy(board.bno.desc()); // 직접 코드로 처리하면

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Member.class, "member1");
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







