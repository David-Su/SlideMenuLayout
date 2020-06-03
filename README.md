# SlideMenuLayout #
## 介绍
一款可以横向滑动的抽屉菜单小控件，菜单是任意自定义的布局，适合在列表中使用。
<img src="https://github.com/David-Su/SlideMenuLayout/blob/master/doc/base.gif" align=center width="40%" height="40%">  <img src="https://github.com/David-Su/SlideMenuLayout/blob/master/doc/extension.gif" align=center width="40%" height="40%">
  
## 用法
### Android Studio依赖
#### Step 1. Add the JitPack repository to your build file,in your root build.gradle at the end of repositories  
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
#### Step 2. Add the dependency
	dependencies {
	        implementation 'com.github.David-Su:SlideMenuLayout:1.0.0'
	}
### XML中使用
	<?xml version="1.0" encoding="utf-8"?>
	<com.davidsu.slidemenulayout.SlideMenuLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    app:menuLayout="@layout/layout_slide_menu">
	
	    <androidx.constraintlayout.widget.ConstraintLayout
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content">
		....
	    </androidx.constraintlayout.widget.ConstraintLayout>
	
	</com.davidsu.slidemenulayout.SlideMenuLayout>


