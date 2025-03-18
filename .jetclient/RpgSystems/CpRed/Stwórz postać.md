```toml
name = 'Stwórz postać'
description = 'Tworzy nową grywalną postać w systemie cyberpunka'
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/games/cpRed/characters/create'
sortWeight = 1000000
id = '6fc6c286-01db-4216-9ea8-72e07d1bb0ea'

[body]
type = 'JSON'
raw = '''
{
  "game": {
    "id": 1
  },
  "name": "Johnny Silverhand",
  "nickname": "Silverhand",
  "type": "PLAYER",
  "expAll": 1000,
  "expAvailable": 500,
  "cash": 2000
}'''
```
