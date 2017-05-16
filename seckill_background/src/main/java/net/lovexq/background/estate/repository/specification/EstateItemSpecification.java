package net.lovexq.background.estate.repository.specification;

import net.lovexq.background.estate.model.EstateItemModel;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 房源复杂查询
 *
 * @author LuPindong
 * @time 2017-04-25 20:43
 */
public class EstateItemSpecification {

    public static Specification<EstateItemModel> getForSaleSpec(Map<String, Object> paramMap) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            Path<String> path = root.get("saleState");
            Predicate predicate = criteriaBuilder.equal(path, "在售");

            if (MapUtils.isNotEmpty(paramMap)) {
                // 区域
                String regionAName = MapUtils.getString(paramMap, "regionAName");
                if (StringUtils.isNotBlank(regionAName)) {
                    Path<String> path1 = root.get("regionAName");
                    Predicate predicate1 = criteriaBuilder.equal(path1, regionAName);
                    predicate = criteriaBuilder.and(predicate, predicate1);
                }

                // 售价
                String totalPrice = MapUtils.getString(paramMap, "totalPrice");
                if (StringUtils.isNotBlank(totalPrice)) {
                    Path<BigDecimal> exp2 = root.get("totalPrice");
                    Predicate predicate2 = null;

                    if ("tp1".equals(totalPrice)) {
                        predicate2 = criteriaBuilder.lessThanOrEqualTo(exp2, new BigDecimal(100));
                    } else if ("tp2".equals(totalPrice)) {
                        predicate2 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp2, new BigDecimal(100)), criteriaBuilder.lessThanOrEqualTo(exp2, new BigDecimal(200)));
                    } else if ("tp3".equals(totalPrice)) {
                        predicate2 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp2, new BigDecimal(200)), criteriaBuilder.lessThanOrEqualTo(exp2, new BigDecimal(300)));
                    } else if ("tp4".equals(totalPrice)) {
                        predicate2 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp2, new BigDecimal(300)), criteriaBuilder.lessThanOrEqualTo(exp2, new BigDecimal(500)));
                    } else if ("tp5".equals(totalPrice)) {
                        predicate2 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp2, new BigDecimal(500)), criteriaBuilder.lessThanOrEqualTo(exp2, new BigDecimal(1000)));
                    } else if ("tp6".equals(totalPrice)) {
                        predicate2 = criteriaBuilder.greaterThanOrEqualTo(exp2, new BigDecimal(1000));
                    }

                    predicate = criteriaBuilder.and(predicate, predicate2);
                }

                // 户型
                String model = MapUtils.getString(paramMap, "model");
                if (StringUtils.isNotBlank(model)) {
                    Path<String> exp3 = root.get("model");
                    Predicate predicate3 = null;

                    if ("5室+".equals(model)) {
                        predicate3 = criteriaBuilder.and(criteriaBuilder.notLike(exp3, "1室" + "%"),
                                criteriaBuilder.notLike(exp3, "2室" + "%"),
                                criteriaBuilder.notLike(exp3, "3室" + "%"),
                                criteriaBuilder.notLike(exp3, "4室" + "%"),
                                criteriaBuilder.notLike(exp3, "5室" + "%"));
                    } else {
                        predicate3 = criteriaBuilder.like(exp3, model + "%");
                    }

                    predicate = criteriaBuilder.and(predicate, predicate3);
                }

                // 面积
                String area = MapUtils.getString(paramMap, "area");
                if (StringUtils.isNotBlank(area)) {
                    Path<BigDecimal> exp4 = root.get("area");
                    Predicate predicate4 = null;

                    if ("a1".equals(area)) {
                        predicate4 = criteriaBuilder.lessThanOrEqualTo(exp4, new BigDecimal(40));
                    } else if ("a2".equals(area)) {
                        predicate4 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp4, new BigDecimal(40)), criteriaBuilder.lessThanOrEqualTo(exp4, new BigDecimal(60)));
                    } else if ("a3".equals(area)) {
                        predicate4 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp4, new BigDecimal(60)), criteriaBuilder.lessThanOrEqualTo(exp4, new BigDecimal(80)));
                    } else if ("a4".equals(area)) {
                        predicate4 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp4, new BigDecimal(80)), criteriaBuilder.lessThanOrEqualTo(exp4, new BigDecimal(100)));
                    } else if ("a5".equals(area)) {
                        predicate4 = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(exp4, new BigDecimal(100)), criteriaBuilder.lessThanOrEqualTo(exp4, new BigDecimal(144)));
                    } else if ("a6".equals(area)) {
                        predicate4 = criteriaBuilder.greaterThanOrEqualTo(exp4, new BigDecimal(144));
                    }

                    predicate = criteriaBuilder.and(predicate, predicate4);
                }

                // 朝向
                String direction = MapUtils.getString(paramMap, "direction");
                if (StringUtils.isNotBlank(direction)) {
                    Path<String> exp5 = root.get("direction");
                    Predicate predicate5 = criteriaBuilder.like(exp5, "%" + direction + "%");

                    predicate = criteriaBuilder.and(predicate, predicate5);
                }

                // 楼层
                String floor = MapUtils.getString(paramMap, "floor");
                if (StringUtils.isNotBlank(floor)) {
                    Path<String> exp6 = root.get("floor");
                    Predicate predicate6 = criteriaBuilder.like(exp6, floor + "%");

                    predicate = criteriaBuilder.and(predicate, predicate6);
                }
            }

            return predicate;
        };
    }

}