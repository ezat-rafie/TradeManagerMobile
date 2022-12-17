package com.example.trademanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
TextView helpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpText = (TextView) findViewById(R.id.helpText);

        // Text is taken from https://www.investopedia.com/articles/trading/06/daytradingretail.asp
        helpText.setText("1. Knowledge Is Power\n" +
                "In addition to knowledge of day trading procedures, day traders need to keep up with the latest stock market news and events that affect stocks. This can include the Federal Reserve System's interest rate plans, leading indicator announcements, and other economic, business, and financial news.\n" +
                "\n" +
                "So, do your homework. Make a wish list of stocks you'd like to trade. Keep yourself informed about the selected companies, their stocks, and general markets. Scan business news and bookmark reliable online news outlets.\n" +
                "\n" +
                "2. Set Aside Funds\n" +
                "Assess and commit to the amount of capital you're willing to risk on each trade. Many successful day traders risk less than 1% to 2% of their accounts per trade. If you have a $40,000 trading account and are willing to risk 0.5% of your capital on each trade, your maximum loss per trade is $200 (0.5% x $40,000).\n" +
                "\n" +
                "Earmark a surplus amount of funds you can trade with and are prepared to lose.\n" +
                "\n" +
                "3. Set Aside Time\n" +
                "Day trading requires your time and attention. In fact, you'll need to give up most of your day. Don’t consider it if you have limited time to spare.\n" +
                "\n" +
                "Day trading requires a trader to track the markets and spot opportunities that can arise at any time during trading hours. Being aware and moving quickly are key.\n" +
                "\n" +
                "4. Start Small\n" +
                "As a beginner, focus on a maximum of one to two stocks during a session. Tracking and finding opportunities is easier with just a few stocks. Recently, it has become increasingly common to trade fractional shares. That lets you specify smaller dollar amounts that you wish to invest.\n" +
                "\n" +
                "This means that if Amazon shares are trading at $3,400, many brokers will now let you purchase a fractional share for an amount that can be as low as $25, or less than 1% of a full Amazon share.\n" +
                "\n" +
                "5. Avoid Penny Stocks\n" +
                "You're probably looking for deals and low prices but stay away from penny stocks. These stocks are often illiquid and the chances of hitting the jackpot with them are often bleak.\n" +
                "\n" +
                "Many stocks trading under $5 a share become delisted from major stock exchanges and are only tradable over-the-counter (OTC). Unless you see a real opportunity and have done your research, steer clear of these.\n" +
                "\n" +
                "6. Time Those Trades\n" +
                "Many orders placed by investors and traders begin to execute as soon as the markets open in the morning, which contributes to price volatility. A seasoned player may be able to recognize patterns at the open and time orders to make profits. For beginners, though, it may be better to read the market without making any moves for the first 15 to 20 minutes.\n" +
                "\n" +
                "The middle hours are usually less volatile. Then movement begins to pick up again toward the closing bell. Though the rush hours offer opportunities, it’s safer for beginners to avoid them at first.\n" +
                "\n" +
                "7. Cut Losses With Limit Orders\n" +
                "Decide what type of orders you'll use to enter and exit trades. Will you use market orders or limit orders? A market order is executed at the best price available at the time, with no price guarantee. It's useful when you just want in or out of the market and don't care about getting filled at a specific price.\n" +
                "\n" +
                "A limit order guarantees price but not the execution.\n" +
                "1\n" +
                " Limit orders can help you trade with more precision and confidence because you set the price at which your order should be executed. A limit order can cut your loss on reversals. However, if the market doesn't reach your price, your order won't be filled and you'll maintain your position.\n" +
                "\n" +
                "More sophisticated and experienced day traders may employ the use of options strategies to hedge their positions as well.\n" +
                "\n" +
                "8. Be Realistic About Profits\n" +
                "A strategy doesn't need to succeed all the time to be profitable. Many successful traders may only make profits on 50% to 60% of their trades. However, they make more on their winners than they lose on their losers. Make sure the financial risk on each trade is limited to a specific percentage of your account and that entry and exit methods are clearly defined.\n" +
                "\n" +
                "9. Stay Cool\n" +
                "There are times when the stock market tests your nerves. As a day trader, you need to learn to keep greed, hope, and fear at bay. Decisions should be governed by logic and not emotion.\n" +
                "\n" +
                "10. Stick to the Plan\n" +
                "Successful traders have to move fast, but they don't have to think fast. Why? Because they've developed a trading strategy in advance, along with the discipline to stick to it. It is important to follow your formula closely rather than try to chase profits. Don't let your emotions get the best of you and make you abandon your strategy. Bear in mind a mantra of day traders: plan your trade and trade your plan.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.trademanager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuHome:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuAdd:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), AddAssetActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuPerformance:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), OverallPerformanceActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuAbout:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuHelp:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
        }
        return false;
    }
}