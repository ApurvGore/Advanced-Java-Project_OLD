package com.cdac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.OrderItemStatus;
import com.cdac.entity.OrderItem;
import com.cdac.exception.OrderItemServiceException;
import com.cdac.service.OrderItemService;

@RestController
@CrossOrigin
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping("/orderItem/save")
	public OrderItemStatus save(@RequestBody OrderItem orderItem) {
		try {
					orderItemService.addOrUpdateItem(orderItem);
					
					OrderItemStatus status = new OrderItemStatus();
			status.setStatus(true);
			status.setStatusMessage("OrderItem Saved!");
			//status.setOrderId(savedOrder.getOrder_id());
			return status;
			
		}
	
		catch(OrderItemServiceException e) {
			OrderItemStatus status = new OrderItemStatus();
			status.setStatus(false);
			status.setStatusMessage(e.getMessage());
			return status;		
		}
	}
	
	@PostMapping("/orderItem/delete")
	public OrderItemStatus delete(@RequestBody  OrderItem orderItem) {
		
		boolean b = orderItemService.delete(orderItem);
		if(b) {
			
			OrderItemStatus status = new OrderItemStatus();
			status.setStatus(true);
			status.setStatusMessage("Order Item Deleted");
			return status;
		}
		else  {
			OrderItemStatus status = new OrderItemStatus();
			status.setStatus(false);
			status.setStatusMessage("Order Item not Deleted");
			return status;
		}
	}
	
	
}
