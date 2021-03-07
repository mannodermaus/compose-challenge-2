# Wind-Up (ADC Week 2)

<!--- Replace <OWNER> with your Github Username and <REPOSITORY> with the name of your repository. -->
<!--- You can find both of these in the url bar when you open your repository in github. -->
![Workflow result](https://github.com/mannodermaus/compose-challenge-2/workflows/Check/badge.svg)


## :scroll: Description
<!--- Describe your app in one or two sentences -->
The most basic of timers, but at least it's pulsating!
Control the remaining time and toggle the timer's state via buttons
in this simple UI. Fun fact: The color scheme is vaguely inspired by the album art of that recent Autechre album.

## :bulb: Motivation and Context
<!--- Optionally point readers to interesting parts of your submission. -->
<!--- What are you especially proud of? -->
Interfacing with the legacy world through `LocalContext` was a first for me, as was the usage of a `LaunchedEffect`
to achieve vibration when the timer is completed. The Canvas API was a lot of fun to explore and I moved
from using the actual `Canvas` element to a custom `drawBehind` modifier over time. Finally, it was cool
to review the maths stuff for cartesian/polar coordinate conversion again. Gotta love that unit circle.

## :camera_flash: Screenshots
<!-- You can add more screenshots here if you like -->
<img src="/results/screenshot_1.png" width="260">&emsp;<img src="/results/screenshot_2.png" width="260">

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```