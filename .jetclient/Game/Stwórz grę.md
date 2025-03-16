```toml
name = 'Stwórz grę'
description = 'Tworzy grę, której GM to tworzący użytkownik'
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/game/create'
sortWeight = 1000000
id = 'afd7eb11-a228-4cdb-bb17-2f2e0f46b95f'

[body]
type = 'JSON'
raw = '''
{
  "name": "Example Game",
  "description": "This is an example game description."
}'''
```
