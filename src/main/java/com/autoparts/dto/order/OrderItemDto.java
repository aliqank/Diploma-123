package com.autoparts.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

   Long id;
   double price;
   int quantity;
   double total;
   TovarReadDto product;
}
