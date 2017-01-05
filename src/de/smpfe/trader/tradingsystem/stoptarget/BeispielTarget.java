/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2017 Frieder Paape
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package de.smpfe.trader.tradingsystem.stoptarget;

import java.util.ArrayList;

import de.smpfe.trader.chartinstrument.AStopTarget;
import de.smpfe.trader.chartinstrument.Message;
import de.smpfe.trader.chartinstrument.enums.Market;
import de.smpfe.trader.chartinstrument.enums.StopTargetOrderType;
import de.smpfe.trader.data.Dependency;
import de.smpfe.trader.enums.TimeFrame;
import de.smpfe.trader.indicators.EMA;

public class BeispielTarget extends AStopTarget
{
	public BeispielTarget()
	{
		//nur fuer targets!!
		super(true);
		
		this.setReferenceTime(TimeFrame.MIN10);
		try {
			this.changeOrderType(StopTargetOrderType.ENDOFPERIOD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.addDependencies(TimeFrame.MIN10, new Dependency[]{
				new Dependency("de.smpfe.trader.indicators.RenkoSAR"),
				new Dependency("de.smpfe.trader.indicators.ATR"),
				new Dependency("de.smpfe.trader.indicators.MA"),
				new Dependency("de.smpfe.trader.indicators.EMA")
		});
	}

	@Override
	public void interpret(ArrayList<Message> messages) 
	{
		EMA ma10 = (EMA)this.getIndicators(TimeFrame.MIN10)[3];
		
		if(true)//ma10.getEma() > this.getCandle(TimeFrame.MIN10).getClose())
		{
			setTrigger(100);
			setRefPointEntryPrice(true);
		}
		else
		{if(this.getTradeManagement().getCurrentOrderInfo().getPosition()==Market.LONG)
			setTrigger( this.getCandle(TimeFrame.MIN10).getClose()+20);
		else if(this.getTradeManagement().getCurrentOrderInfo().getPosition()==Market.SHORT)
			setTrigger( this.getCandle(TimeFrame.MIN10).getClose()-20);
			
			setRefPointEntryPrice(false);
		}
			
		
	}

	@Override
	public boolean updateOnChartData() {
		
		return true;
	}

	@Override
	public boolean updateOnSentimentChange() {
		
		return true;
	}

	@Override
	public boolean updateOnStopTargetChange() {
		
		return false;
	}

	@Override
	public boolean updateOnFilterChange() {
		
		return false;
	}

	@Override
	public boolean updateOnOrderFeedback() {
		
		return false;
	}

}
