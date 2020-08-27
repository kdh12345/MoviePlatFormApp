# MoviePlatFormApp

사용자에게 영화에 대한 정보를 제공하는 앱

서버에 저장된 영화의 한줄평, 예매율, 작가, 배우 등의 상세정보를 제공하는 화면
예매율순/큐레이션/상영예정일 순으로 영화를 정렬할 수 있습니다.

라이브러리:
implementation 'com.android.volley:volley:1.1.1' RequestQueue를 이용해서 서버에 접근하기 위한 라이브러리
implementation 'com.google.code.gson:gson:2.8.5' 서버에서 받아온 데이터를 json형태로 변환
implementation 'com.github.bumptech.glide:glide:3.7.0' //서버에서 받아온 이미지를 보여주기 위한 라이브러리
implementation 'com.github.chrisbanes:PhotoView:2.3.0' // 사진의 손가락으로 확대/축소하기 위해 사용한 라이브러리
