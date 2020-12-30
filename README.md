# Socket.io-Chat-App
<p>This is a simple Real-time chat app using socket.io. You can connect to <a href="http://socket.io/blog/native-socket-io-and-android/" rel="nofollow">socket-io</a> using this app.</p>

<h1><a id="user-content--features-project-android" class="anchor" aria-hidden="true" href="#-features-project-android"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z"></path></svg></a><g-emoji class="g-emoji" alias="sparkles" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2728.png">✨</g-emoji>Client Side</h1>
<ul>
<li>100% Kotlin</li>
<li>MVVM architecture</li>
<li>Android architecture components</li>
<li>Navigation Jetpack</li>
<li>Single activity</li>
<li>dataBinding</li>
<li>Coroutines</li>
</ul>

<h1><a id="user-content--features-project-android" class="anchor" aria-hidden="true" href="#-features-project-android"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z"></path></svg></a><g-emoji class="g-emoji" alias="sparkles" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2728.png">✨</g-emoji>Server Side:
</h1>
<ul>
<li>Node js</li>
<li>Add User</li>
<li>Add Groups</li>
<li>Update Profile</li>
<li>Update User Status "online/offline"</li>
</ul>
<br>

<img src="https://github.com/Ibrahim-Mushtaha/Socket.io-Chat-App/blob/master/app/src/main/res/drawable/ic_image1.png" alt="Group 3121" style="max-width:100%;"><br>

Installing the Dependencies The first step is to install the Java Socket.IO client with Gradle.

For this app, we just add the dependency to build.gradle:<br>
<pre>
dependencies {
    implementation <span class="pl-s"><span class="pl-pds">'</span>com.github.nkzawa:socket.io-client:0.6.0<span class="pl-pds">'</span></span>
}</pre>
