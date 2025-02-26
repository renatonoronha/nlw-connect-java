package br.dev.renatofreitas.events.repository;

import br.dev.renatofreitas.events.dto.SubscriptionRankingItem;
import br.dev.renatofreitas.events.model.Event;
import br.dev.renatofreitas.events.model.Subscription;
import br.dev.renatofreitas.events.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User subscriber);

    // :eventId -> parâmetro
    @Query(value = "select count(subscription_number) as quantidade, indication_user_id, user_name "
            + " from db_events.tbl_subscription inner join db_events.tbl_user "
            + " on tbl_subscription.indication_user_id = tbl_user.user_id "
            + " where indication_user_id is not null "
            + " and event_id = :eventId "
            + " group by indication_user_id "
            + " order by quantidade desc", nativeQuery = true)
    public List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId); // mapeando os parâmetros
}
