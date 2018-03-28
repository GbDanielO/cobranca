$(function() {
	$('[rel="tooltip"]').tooltip();
	
	$('.js-atualizar-status').on('click', function(event){
		event.preventDefault();
		
		var link = $(event.currentTarget);
		var url = link.attr('href');
		
		var response = $.ajax({
			url: url,
			type: 'PUT'
			
		});
		
		response.done(function(data){
			var codigo = link.data('codigo');
			link.hide();
			console.log(data);
			$('[data-role=' + codigo + ']').html('<span class="label label-success">'+data+'</span>');
		});
		
		response.fail(function(e){
			alert('Erro ao dar baixa na cobrança.');
		});
	});
	
})

