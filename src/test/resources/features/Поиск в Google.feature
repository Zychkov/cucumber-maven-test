# language: ru

@example
Функция: Поиск в Google

  @isolated
  Сценарий: Поиск фразы "Cucumber framework"
    Когда перехожу по url "https://www.google.ru/"
    И в ввожу в строку поиска "Cucumber framework"
    Тогда вижу ссылку "https://cucumber.io/"