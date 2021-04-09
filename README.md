# A Tour

### Главный экран с фрагментами (Мои, Список, Управляю, Поиск) MainActivity
отображает соответствующие списки чемпионатов

Мои - там, где пользователь принимает участие
Список - общий, 30 последних чемпионатов
Управляю - чемпионаты, созданные пользователем
Поиск - поиск по названию чемпионата


### Регистрация RegistrationActivity
	-ФИО
	-город
	-телефон
	-почта (40сим)
	-пароль (40сим)

### Аутентификация LoginActivity
	-логин (40сим)
	-пароль (40сим)

### Профиль ProfileActivity
отображает регистрационные данные 

### Создание чемпионата ChampCreationActivity
	-название (40сим)
	-дата начала
	-дата окончания
	-город
	-статус (Чемпионат, Кубок, Перенство, Всероссийские)
	-виды (Пеший, Лыжный, Горный, Водный, Спелео, Вело, АвтоМото, Прочее)

### Подача заявки на чемпионат
	-выбор роли на чемпионате (Участник или Судья)
	-виды (Пеший, Лыжный, Горный, Водный, Спелео, Вело, АвтоМото, Прочее)
	-комметарий
	-ссылка на облачное хранилище

### Заявки
	экран со списком заявок на чемпионат 
		- роль
		- ФИО
		- виды
	доступен только создателю чемпионата
	находится в правом верхнем углу на экране Чемпионата