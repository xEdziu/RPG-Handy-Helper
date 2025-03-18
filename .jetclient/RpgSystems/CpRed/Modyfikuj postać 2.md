```toml
name = 'Modyfikuj postać 2'
description = 'Usuwa usera'
method = 'PUT'
url = 'http://localhost:8888/api/v1/authorized/games/cpRed/characters/update/1'
sortWeight = 5000000
id = '8595f81c-9831-4056-b5a8-09c8b3b50d5b'

[body]
type = 'JSON'
raw = '''
{
  "user": null,
  "type": "NPC",
}'''
```
