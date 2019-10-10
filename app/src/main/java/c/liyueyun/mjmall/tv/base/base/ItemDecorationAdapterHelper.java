package c.liyueyun.mjmall.tv.base.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * BaseAdapterRecycleViewhelper专用itemDecoration
 */

public class ItemDecorationAdapterHelper extends RecyclerView.ItemDecoration {

    private String TAG = "ItemDecorationAdapterHelper";
    private int mSpaceHorizontal;
    private int mSpaceVertical;
    private Paint mPaint;
    private boolean includeTopAndBottom;
    private boolean hasHeader = true;
    private int headerCount = 0;
    private int footerCount = 0;

    public ItemDecorationAdapterHelper(Context context, int spaceHorizontal, int spaceVertical, boolean isAddFirstAndEnd, int headerCount) {
        mSpaceHorizontal = Tool.getDimenWidth(context, spaceHorizontal);
        mSpaceVertical = Tool.getDimenWidth(context, spaceVertical);
        includeTopAndBottom = isAddFirstAndEnd;
        this.headerCount = headerCount;
        if (headerCount > 0) {
            this.hasHeader = true;
        } else {
            this.hasHeader = false;
        }
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    //画线颜色
    public ItemDecorationAdapterHelper(Activity activity, int color, int spaceHorizontal, int spaceVertical, boolean isAddFirstAndEnd, int headerCount) {
        mSpaceHorizontal = Tool.getDimenWidth(activity, spaceHorizontal);
        mSpaceVertical = Tool.getDimenhight(activity, spaceVertical);
        includeTopAndBottom = isAddFirstAndEnd;
        this.headerCount = headerCount;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(activity, color));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
//            drawGridItemSpace(c, parent, (GridLayoutManager) layoutManager);
        } else if (layoutManager instanceof LinearLayoutManager) {
            drawLinearItemSpace(c, parent, (LinearLayoutManager) layoutManager);

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            layoutGridItem(outRect, view, parent, (GridLayoutManager) layoutManager);
        } else if (layoutManager instanceof LinearLayoutManager) {
            layoutLinearItem(outRect, view, parent, (LinearLayoutManager) layoutManager);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            layoutStaggeredItem(outRect, view, parent, (StaggeredGridLayoutManager) layoutManager);
        }
    }

    //GridItemDecoration
    private void layoutGridItem(Rect outRect, View view, RecyclerView parent, GridLayoutManager gridManager) {

        boolean ver = gridManager.getOrientation() == GridLayoutManager.VERTICAL;
        int viewPosition = parent.getChildAdapterPosition(view);
        int spanCount = gridManager.getSpanCount();
        int index = (viewPosition - headerCount) % spanCount;
        if (viewPosition < headerCount) {
//            logUtil.d_2(TAG,"viewPosition="+viewPosition+"---"+"头部");
            outRect.top = 0;
            outRect.bottom = 0;
            outRect.right = 0;
            outRect.left = 0;
        } else if (viewPosition >= headerCount && viewPosition <spanCount) {//第一行
            if (includeTopAndBottom) {
                if (ver) {
                    outRect.top = mSpaceVertical/2;
                } else {
                    //水平方向滑动
                    outRect.left = mSpaceHorizontal/2;
                }
            } else {
                if (ver) {
                    outRect.top = 0;
                } else {
                    outRect.left = 0;
                }
            }

            if (ver) {
                outRect.bottom = mSpaceVertical / 2;
            } else {
                outRect.right = mSpaceHorizontal / 2;
            }
//            logUtil.d_2(TAG, "viewPosition=" + viewPosition + "---" + "第一行");

        } else if ((viewPosition - headerCount) / spanCount == (gridManager.getItemCount() - 1 - headerCount) / spanCount) {//多宫格如何判断最后一行？
//            logUtil.d_2(TAG, "viewPosition=" + viewPosition + "---" + "最后一行");
            if (includeTopAndBottom) {
                if (ver) {
                    outRect.bottom = mSpaceVertical/2;
                } else {
                    outRect.left = mSpaceHorizontal/2;
                }
            } else {
                if (ver) {
                    outRect.bottom = 0;
                } else {
                    outRect.left = 0;
                }
            }
            if (ver) {
                outRect.top = mSpaceVertical / 2;
            } else {
                outRect.right = mSpaceHorizontal / 2;
            }
        } else {
//            logUtil.d_2(TAG, "viewPosition=" + viewPosition + "---" + "其他");
            if (ver) {
                outRect.top = mSpaceVertical / 2;
                outRect.bottom = mSpaceVertical / 2;
            } else {
                outRect.left = mSpaceHorizontal / 2;
                outRect.right = mSpaceHorizontal / 2;
            }
        }


        //列
        if (viewPosition < headerCount) {
            //头部
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        } else if (index == 0) {//第一列
            if (includeTopAndBottom) {
                if (ver) {
                    outRect.left = mSpaceHorizontal/2;
                } else {
                    outRect.top = mSpaceHorizontal/2;
                }
            } else {
                if (ver) {
                    outRect.left = 0;
                } else {
                    outRect.top = 0;
                }
            }
            if (ver) {
                outRect.right = mSpaceHorizontal / 2;
            } else {
                outRect.bottom = mSpaceHorizontal / 2;
            }

        } else if (index == spanCount - 1) {//最后一列
            if (includeTopAndBottom) {
                if (ver) {
                    outRect.right = mSpaceHorizontal/2;
                } else {
                    outRect.bottom = mSpaceHorizontal/2;
                }
            } else {
                if (ver) {
                    outRect.right = 0;
                } else {
                    outRect.bottom = 0;
                }
            }

            if (ver) {
                outRect.left = mSpaceHorizontal / 2;
            } else {
                outRect.top = mSpaceHorizontal / 2;
            }

        } else {
            if (ver) {
                outRect.left = mSpaceHorizontal / 2;
                outRect.right = mSpaceHorizontal / 2;
            } else {
                outRect.top = mSpaceHorizontal / 2;
                outRect.bottom = mSpaceHorizontal / 2;
            }
        }
    }


    private void drawGridItemSpace(Canvas c, RecyclerView parent, GridLayoutManager gridManager) {
        for (int viewPosition = 0; viewPosition < gridManager.getItemCount(); viewPosition++) {
            View childView = parent.getChildAt(viewPosition);

            if (childView == null) {
                continue;
            }

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();

            int spanCount = gridManager.getSpanCount();
            int index = (viewPosition - headerCount) % spanCount;

            int left;
            int top;
            int right;
            int bottom;

            left = childView.getLeft() - params.leftMargin;
            right = childView.getRight() + params.rightMargin + mSpaceHorizontal;
            if (viewPosition >= headerCount && viewPosition <= spanCount) {//第一行
//                XLog.e(TAG, "viewPosition=" + viewPosition + "---" + "第一行");
                top = childView.getTop() - params.topMargin - mSpaceVertical;
                bottom = top + mSpaceVertical;
                c.drawRect(left, top, right, bottom, mPaint);

                top = childView.getBottom() + params.bottomMargin;
                bottom = top + mSpaceVertical;
                c.drawRect(left, top, right, bottom, mPaint);
            } else if (viewPosition < headerCount) {
//                XLog.e(TAG, "viewPosition=" + viewPosition + "---" + "头部");
            } else {
//                XLog.e(TAG, "viewPosition=" + viewPosition + "---" + "其他");
                top = childView.getBottom() + params.bottomMargin;
                bottom = top + mSpaceVertical;
                c.drawRect(left, top, right, bottom, mPaint);
            }


            if (index == 0) {//第一列
                top = childView.getTop() - params.topMargin;
                bottom = childView.getBottom() + params.bottomMargin + mSpaceVertical;
                left = childView.getLeft() - params.leftMargin - mSpaceHorizontal;
                right = left + mSpaceHorizontal;
                c.drawRect(left, top, right, bottom, mPaint);

                top = childView.getTop() - params.topMargin;
                bottom = childView.getBottom() + params.bottomMargin;
                left = childView.getRight() + params.rightMargin;
                right = left + mSpaceHorizontal;
                c.drawRect(left, top, right, bottom, mPaint);

                if (viewPosition == headerCount) {
                    top = childView.getTop() - params.topMargin - mSpaceVertical;
                    bottom = top + mSpaceVertical;
                    left = childView.getLeft() - params.leftMargin - mSpaceHorizontal;
                    right = left + mSpaceHorizontal;
                    c.drawRect(left, top, right, bottom, mPaint);
                }

            } else {
                top = childView.getTop() - params.topMargin;
                bottom = childView.getBottom() + params.bottomMargin;
                left = childView.getRight() + params.rightMargin;
                right = left + mSpaceHorizontal;
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //LinerLayoutItemDecoration
    private void layoutLinearItem(Rect outRect, View view, RecyclerView parent, LinearLayoutManager linearManager) {
        int viewPosition = parent.getChildAdapterPosition(view);
        if (linearManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            //竖直方向
            outRect.left = mSpaceHorizontal;
            outRect.right = mSpaceHorizontal;
            if (viewPosition < headerCount) {
                //头部
                outRect.top = 0;
                outRect.bottom = 0;

            } else if (viewPosition == headerCount) {
                //第一个item
                if (includeTopAndBottom) {
                    //包边
                    outRect.top = mSpaceVertical;
                }
                outRect.bottom = mSpaceVertical / 2;

            } else if (viewPosition == linearManager.getItemCount() - 1) {
                outRect.top = mSpaceVertical / 2;
                if (includeTopAndBottom) {
                    outRect.bottom = mSpaceVertical;
                }
            } else {
                outRect.top = mSpaceVertical / 2;
                outRect.bottom = mSpaceVertical / 2;
            }
        } else {
            //水平方向
            outRect.top = mSpaceVertical;
            outRect.bottom = mSpaceVertical;

            if (viewPosition == 0) {
                if (hasHeader) {
                    outRect.left = 0;
                    outRect.right = 0;
                } else {
                    if (includeTopAndBottom) {
                        outRect.left = mSpaceHorizontal;
                    }
                    outRect.right = mSpaceHorizontal / 2;
                }
            } else if (viewPosition == linearManager.getItemCount() - 1) {
                outRect.left = mSpaceHorizontal / 2;
                if (includeTopAndBottom) {
                    outRect.right = mSpaceHorizontal;
                }
            } else {
                outRect.left = mSpaceHorizontal / 2;
                outRect.right = mSpaceHorizontal / 2;
            }
        }

    }

    private void drawLinearItemSpace(Canvas c, RecyclerView parent, LinearLayoutManager linearManager) {

        if (linearManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            for (int viewPosition = 0; viewPosition < linearManager.getItemCount(); viewPosition++) {
                View childView = parent.getChildAt(viewPosition);

                if (childView == null) {
                    continue;
                }
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int left;
                int top;
                int right;
                int bottom;
                left = childView.getLeft() - params.leftMargin;
                right = childView.getRight() + params.rightMargin;
                top = childView.getBottom() + params.bottomMargin;
                bottom = top + mSpaceVertical;
                c.drawRect(left, top, right, bottom, mPaint);
                if (viewPosition == 0) {
                    if (includeTopAndBottom) {
                        //最上最下也绘线
                        top = childView.getTop() - params.topMargin - mSpaceVertical;
                        bottom = top + mSpaceVertical;
                    }
                    c.drawRect(left, top, right, bottom, mPaint);
                }
            }
        } else {
            for (int viewPosition = 0; viewPosition < linearManager.getItemCount(); viewPosition++) {
                View childView = parent.getChildAt(viewPosition);

                if (childView == null) {
                    continue;
                }

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();

                int left;
                int top;
                int right;
                int bottom;

                left = childView.getRight() + params.rightMargin;
                top = childView.getTop() - params.topMargin;
                right = left + mSpaceHorizontal;
                bottom = childView.getBottom() + params.bottomMargin;
                c.drawRect(left, top, right, bottom, mPaint);

                if (viewPosition == 0) {
                    if (includeTopAndBottom) {
                        //最左最右绘线
                        left = childView.getLeft() - params.leftMargin - mSpaceHorizontal;
                        right = left + mSpaceHorizontal;
                    }
                    top = childView.getTop() - params.topMargin;
                    bottom = childView.getBottom() + params.bottomMargin;
                    c.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }

    /**
     * 瀑布流添加item间距
     */

    private StaggeredGridLayoutManager.LayoutParams params;

    private void layoutStaggeredItem(Rect outRect, View view, RecyclerView parent, StaggeredGridLayoutManager layoutManager) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == 0) {
            return;
        }
        int position = parent.getChildAdapterPosition(view);

        params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

//        if (position < 2) {
//            // 用于设第一个位置跟顶部的距离
//            outRect.top = mSpaceHorizontal;
//        }

        //在item中设置相等的左边距和上边距

        Log.e("clp1", "position=" + position + "clp1=" + params.getSpanIndex());
        if (params.getSpanIndex() == 0) {
            // 用于设同行两个间隔间跟其距离左右屏幕间隔相同
            outRect.left = 0;
            outRect.right = 0;
        } else {
            //只需要设置第二列的右边距
            outRect.right = mSpaceHorizontal;
        }
    }


}
