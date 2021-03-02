package br.com.microservice.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.microservice.data.vo.ProdutoVO;

@Component
public class ProdutoSendMessage {

	@Value("${produto.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${produto.rabbitmq.routingkey}")
	private String routingkey;
	
	private RabbitTemplate rabbitTemplate;

	@Autowired
	public ProdutoSendMessage(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void send(ProdutoVO produtoVO) {
		this.rabbitTemplate.convertAndSend(exchange, routingkey, produtoVO);
	}
	
	
}
