# NLW Connect - Java

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

## Descrição
Esse projeto é uma API de um sistema de inscrições em eventos feita com Java, Spring Boot e com MySQL como banco de dados. Essa API foi desenvolvida durante o evento da NLW Connect organizado pela Rocketseat e com o auxílio do professor Isidro.

##  API Endpoints
### POST /events
Cria um novo evento

## Pré-requisitos

- Docker 27.5.1
- MySQL Workbench 8.0.41
- Postman

## Instalação

- No terminal entre na pasta data e rode o comando `docker compose up`. A partir daí o Docker irá baixar a imagem do MySQL e iniciar o container
- No MySQL Workbench crie uma nova conexão, você pode colocar o nome que quiser. Para testar a conexão clique em test connection na primeira página que apareceu, você deve inserir a senha definida no arquivo docker-compose.yml 'mysql'. Se aparecer uma tela com um aviso de connection warning clique em continue anyway, se tudo der certo, deve aparecer uma mensagem dizendo que a conexão foi feita com sucesso, em seguida é só clicar ok nessa página e ok na página que inserimos o nome da conexão.
- Quando você abrir a conexão clique em File (ou arquivo) e Open SQL Script (ou Ctrl + Shift + O), em seguida você deve procurar o arquivo db_events.sql dentro da pasta data do projeto e selecioná-la. Quando o script tiver sido importada, rode o script clicando no raio mais a esquerda
  Pronto seu banco de dados está pronto, agora você pode iniciar sua aplicação e testar enviando requisições no postman

Requisição
```
{ 
	"name":"CodeCraft Summit 2025", 
	"location":"Online", 
	"price":0.0, 
	"startDate":"2025-03-16", 
	"endDate":"2025-03-18", 
	"startTime":"19:00:00" 
	"endTime":"21:00:00" 
}
```

Resposta
```
{ 
	"id": 1, 
	"name":"CodeCraft Summit 2025", 
	"prettyName":"codecraft-summit-2025", 
	"location":"Online", "price":0.0, 
	"startDate":"2025-03-16", 
	"endDate":"2025-03-18", 
	"startTime":"19:00:00", 
	"endTime":"21:00:00" 
}
```

### GET /events
Lista todos os eventos

Resposta

```
[
	{ 
		"id": 1, 
		"name":"CodeCraft Summit 2025", 
		"prettyName":"codecraft-summit-2025", 
		"location":"Online", 
		"price":0.0, 
		"startDate":"2025-03-16", 
		"endDate":"2025-03-18", 
		"startTime":"19:00:00", 
		"endTime":"21:00:00" 
	}, 
	... 
]
```

### GET /events/PRETTY_NAME
Recupera um evento pelo seu Pretty Name

Exemplo: `http://localhost:8080/events/codecraft-summit-2025`

Resposta
```
{ 
	"id": 1, 
	"name":"CodeCraft Summit 2025", 
	"prettyName":"codecraft-summit-2025", 
	"location":"Online", 
	"price":0.0, 
	"startDate":"2025-03-16", 
	"endDate":"2025-03-18", 
	"startTime":"19:00:00", 
	"endTime":"21:00:00" 
}
```

### POST /subscription/PRETTY_NAME
Realiza a inscrição em um evento

Requisição esperada

```
{ 
	"userName":"John Doe", 
	"email":"john@doe.com" 
}
```

Resposta esperada

```b
{ 
	"subscriptionNumber":1, 
	"designation": "https://devstage.com/codecraft-summit-2025/123" 
}
```

### GET /subscription/PRETTY_NAME/ranking

Exemplo: `//localhost:8080/subscription/codecraft-summit-2025/ranking`

Resposta

```
[ 
	{ 
		"userName":"John Doe", 
		"subscribers":1000 
	}, 
	{ 
		"userName":"Mary Page", 
		"subscribers":873 
	}, 
	{ 
		"userName":"Frank Lynn", 
		"subscribers":690 
	}
]
```

### GET /subscription/PRETTY_NAME/ranking/USERID
Recupera o número de inscritos que efetivaram sua participação no evento indicados por um determinado usuário (UserId), bem como sua colocação no ranking geral

Exemplo: `//localhost:8080/subscription/codecraft-summit-2025/ranking/123`

Resposta

```
{ 
	"rankingPosition":3, 
	{ 
		"userId":123, 
		"name":"John Doe", 
		"count":600 
	} 
}
```

