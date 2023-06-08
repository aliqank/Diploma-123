package com.autoparts.entity.order;

import com.autoparts.entity.Tovar;
import lombok.*;

import javax.persistence.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tovar_id")
    private Tovar tovar;

    private double price;
    private int quantity;
    private double total;
}
