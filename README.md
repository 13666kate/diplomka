#### Sighted People-это мобильное приложение под систему Android, предназначенное для помощи слабовидящим людям. Приложение имеет два типа пользователей - волонтера и слабовидяшего человека. Волонтер - это человек от организации волонтеров. Он выполняет поддерживающий характер-Экстренная помощь с помощью специальной кнопки SOS, чата, звонока, видеозвонока.
#### Волонтеры  и  их подопечные могут зарегистрироваться в этом приложении и быть всегда на связи. Плюсы этого приложения- то что продопечному можно получить помощь от своего волонтера, предварительно выбранного и добавленного в друзья, в одном месте с поддержкой голосового сопровожднения, и дополнительных функций лично для него. 
#### Данный проект выполнен с использованием Jetpack Compose.

#### В проекте имеется  функция к подключению клавиатуры брайля.В данном случае это физическое устройсво, которое подключается к приложению  по Wi-fi с помощью модуля NodeMCU (ESP8266-V3) и поворотного энкодера. Устройства(модуль и телефон) должны быть подключены к одной сети. При повороте энкодера выбирается буква по порядку алфавита, при нажатии на кнопку энкодера, буква заносится в строку и так по кругу. 
#### Прошивка для клавиатуры брайля- https://github.com/13666kate/Diplom-NodeMCU.

#### Функция распознавания текста с камеры (OCR) -  При нажатии на кнопку открывается камера, при наведении камеры на текст, идет распознавнаие символов. После распознавания текст озвучивается. Так же можно сначала сделать фотографию или выбрать ее с галереи и конвертировать в текст. Распожнавание текста доступно на трех языках-English, Русский, Кыргызча. 



