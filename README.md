##概述

在Android系统API中，有两个Camera类：

    android.graphics.Camera
    android.hardware.Camera

第二个应用于手机硬件的中相机相关的操作，本文讲述的是利用第一个Camera类实现中轴3D转换的卡牌翻转效果.

对应于三维坐标系中的三个方向，Camera提供了三种旋转方法：

    rotateX()
    rotateY()
    rotateX()

调用这三种方法，传入旋转角度参数，即可实现视图沿着坐标轴旋转的功能。

####这里也贴下Rotate3dAnimation 的代码

```

/**
 * An animation that rotates the view on the Y axis between two specified angles.
 * This animation also adds a translation on the Z axis (depth) to improve the effect.
 */
public class Rotate3dAnimation extends Animation {
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final boolean mReverse;
    private Camera mCamera;

    /**
     * Creates a new 3D rotation on the Y axis. The rotation is defined by its
     * start angle and its end angle. Both angles are in degrees. The rotation
     * is performed around a center point on the 2D space, definied by a pair
     * of X and Y coordinates, called centerX and centerY. When the animation
     * starts, a translation on the Z axis (depth) is performed. The length
     * of the translation can be specified, as well as whether the translation
     * should be reversed in time.
     *
     * @param fromDegrees the start angle of the 3D rotation //起始角度
     * @param toDegrees the end angle of the 3D rotation //结束角度
     * @param centerX the X center of the 3D rotation //x中轴线
     * @param centerY the Y center of the 3D rotation //y中轴线
     * @param reverse true if the translation should be reversed, false otherwise//是否反转
     */
    public Rotate3dAnimation(float fromDegrees, float toDegrees,
            float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;//Z轴移动的距离，这个来影响视觉效果，可以解决flip animation那个给人看似放大的效果
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        Log.i("interpolatedTime", interpolatedTime+"");
        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
```
可以看出， Rotate3dAnimation 总共做了两件事：在构造函数中赋值了旋转动画所需要的参数，以及重写（override）父类Animation中的applyTransformation()方法，下面分类阐述一下：

#####    fromDegrees与toDegrees
    视图旋转的开始角度和结束角度，当toDegree处于90倍数时，视图将变得不可见。

#####    centerX与centerY
    视图旋转的中心点。

#####    depthZ
    Z轴移动基数，用于计算Camera在Z轴移动距离

#####    reverse
    boolean类型，控制Z轴移动方向，达到视觉远近移动导致的视图放大缩小效果。

#####    applyTransformation()
    根据动画播放的时间 interpolatedTime （动画start到end的过程，interpolatedTime从0.0变化到1.0），让Camera在Z轴方向上进行相应距离的移动，实现视觉上远近移动的效果。然后调用 rotateX()方法，让视图围绕Y轴进行旋转，产生3D立体旋转效果。最后再通过Matrix来确定旋转的中心点的位置。


## Copyright Notice
```
Copyright (C) 2017 pao11

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


