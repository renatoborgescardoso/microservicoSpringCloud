package br.com.microservice.pagamento.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.microservice.pagamento.data.vo.ProdutoVO;
import br.com.microservice.pagamento.entity.Produto;
import br.com.microservice.pagamento.repository.ProdutoRepository;

@Component
public class ProdutoReceiveMessage {

	private ProdutoRepository produtoRepository;
	
	public ProdutoReceiveMessage(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	@RabbitListener(queues = {"${produto.rabbitmq.queue}"})
	public void receive(@Payload ProdutoVO produtoVO) {
		this.produtoRepository.save(Produto.create(produtoVO));
	}
}
