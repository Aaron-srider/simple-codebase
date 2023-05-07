# CreateArticle

```json
{
  "title": {
    "type": "string",
    "required": true,
    "description": "title of the article"
  },
  "createTime": {
    "type": "string",
    "required": true,
    "description": "create time of the article"
  },
  "snippets": {
    "type": "list<$typeref=CreateSnippet>",
    "required": true,
    "description": "snippets of the article"
  }
}
```

# CreateSnippet

```json
{
  "content": {
    "type": "string",
    "required": true,
    "description": "code"
  },
  "order": {
    "type": "number",
    "required": true,
    "description": "order of the snippet, start from 0"
  },
  "lang": {
    "type": "string",
    "required": false,
    "default": null,
    "description": "snippets of the article"
  },
  "description": {
    "type": "string",
    "required": false,
    "default": null,
    "description": "description of the snippet"
  }
}
```

# UpdateArticle

```json
{
  "title": {
    "type": "string",
    "required": false,
    "description": "title of the article"
  },
  "snippets": {
    "type": "list<$typeref=UpdateSnippet>",
    "required": true,
    "description": "snippets of the article"
  }
}
```

# UpdateSnippet

```json
{
  "id": {
    "type": "number",
    "required": true,
    "description": "id of the snippet"
  },
  "content": {
    "type": "string",
    "required": false,
    "default": null,
    "description": "code"
  },
  "order": {
    "type": "number",
    "required": false,
    "default": null,
    "description": "order of the snippet, start from 0"
  },
  "lang": {
    "type": "string",
    "required": false,
    "default": null,
    "description": "snippets of the article"
  },
  "description": {
    "type": "string",
    "required": false,
    "default": null,
    "description": "description of the snippet"
  }
}
```

# ArticleVO

```json
{
  "id": {
    "type": "number",
    "description": "id of the article"
  },
  "title": {
    "type": "string",
    "description": "title of the article"
  },
  "createTime": {
    "type": "string",
    "description": "create time of the article"
  },
  "snippets": {
    "type": "list<$typeref=SnippetVO>",
    "description": "snippets of the article"
  }
}
```

# SnippetVO

```json
{
  "id": {
    "type": "number",
    "description": "id of the snippet"
  },
  "content": {
    "type": "string",
    "description": "code"
  },
  "order": {
    "type": "number",
    "description": "order of the snippet, start from 0"
  },
  "lang": {
    "type": "string",
    "description": "snippets of the article"
  },
  "description": {
    "type": "string",
    "description": "description of the snippet"
  }
}
```

