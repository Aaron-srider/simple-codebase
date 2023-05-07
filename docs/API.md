# Artifact

## Prefix

```
/API
```

## Create A Artifact

### URL

```
POST /article
```

### Parameter

| Name | Type | Description |
| --- | --- | --- |

### Body

```
$typeref=CreateArticle
```

### Return

```json
{
  "articleId": {
    "type": "string",
    "description": "id of the new article"
  }
}
```

## Delete A Article

### URL

```
DELETE /article/${articleId}
```

### Parameter

| Name | Type | Description |
| --- | --- | --- |
| articleId | number | id of the article |


### Response Code

| Code | Description       |
| --- |-------------------|
| ARTICLE_NOT_FOUND | article not found |


## Update A Artifact

### URL

```
PUT /article/${articleId}
```

### Parameter

| Name | Type | Description |
| --- | --- | --- |
| articleId | number | id of the article |

### Body

```default
$typeref=UpdateArticle
```

## List Artifact

### URL

```
GET /articles
```

### Parameter

| Name     | Type   | Description |
|----------|--------|-------|
| pageNo   | number |  |
| pageSize | number |  |
| title    | string | query articles by title (%title%) |

### Return

```

list<$typeref=ArticleVO>

```

## List Snippets for A Article

### URL

```
GET /article/snippets/${articleId}
```

### Parameter

| Name     | Type   | Description   |
|----------|--------|---------------|
| articleId   | number | id of article |

### Return

```

list<$typeref=SnippetVO>

```
