```toml
name = 'Dodaj system for admin'
description = 'Dodaje nowy system'
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/admin/rpgSystems/create'
sortWeight = 1000000
id = 'aeb3cdfd-d76e-4c2e-ad96-b3346a1f4c2f'

[body]
type = 'JSON'
raw = '''
{
  "name": "Dungeons & Dragons",
  "description": "A fantasy tabletop role-playing game."
}'''
```
