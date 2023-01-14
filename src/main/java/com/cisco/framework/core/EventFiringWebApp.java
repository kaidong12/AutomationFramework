package com.cisco.framework.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante
 */
public class EventFiringWebApp extends WebApp {

	private WebDriverEventListener	webDriverEventListenerAfterChangeValueOf	= null;
	private WebDriverEventListener	webDriverEventListenerAfterClickOn			= null;
	private WebDriverEventListener	webDriverEventListenerAfterFindBy			= null;
	private WebDriverEventListener	webDriverEventListenerAfterNavigateBack		= null;
	private WebDriverEventListener	webDriverEventListenerAfterNavigateForward	= null;
	private WebDriverEventListener	webDriverEventListenerAfterNavigateTo		= null;
	private WebDriverEventListener	webDriverEventListenerAfterScript			= null;

	private WebDriverEventListener	webDriverEventListenerBeforeChangeValueOf	= null;
	private WebDriverEventListener	webDriverEventListenerBeforeClickOn			= null;
	private WebDriverEventListener	webDriverEventListenerBeforeFindBy			= null;
	private WebDriverEventListener	webDriverEventListenerBeforeNavigateBack	= null;
	private WebDriverEventListener	webDriverEventListenerBeforeNavigateForward	= null;
	private WebDriverEventListener	webDriverEventListenerBeforeNavigateTo		= null;
	private WebDriverEventListener	webDriverEventListenerBeforeScript			= null;

	private WebDriverEventListener	webDriverEventListenerOnException			= null;

	/**
	 * @param log
	 * @param browser
	 */
	public EventFiringWebApp(Log log, Browser browser) {
		super(log, browser);
		init();
	}

	// --- START: PUBLIC METHOD SECTION -----------------------------------------------------------

	// --- START: REGISTER SECTION ----------------------------------------------------------------
	public EventFiringWebApp registerAllEvents() {
		registerBeforeChangeOnValueEvent();
		registerBeforeClickOnEvent();
		registerBeforeFindByEvent();
		registerBeforeNavigateBackEvent();
		registerBeforeNavigateForwardEvent();
		registerBeforeNavigateToEvent();
		registerBeforeScriptEvent();

		registerAfterChangeValueOfEvent();
		registerAfterClickOnEvent();
		registerAfterFindByEvent();
		registerAfterNavigateBackEvent();
		registerAfterNavigateForwardEvent();
		registerAfterNavigateToEvent();
		registerAfterScriptEvent();

		registerOnExceptionEvent();
		return this;
	}

	public EventFiringWebApp unRegisterAllEvents() {
		unRegisterBeforeChangeOnValueEvent();
		unRegisterBeforeClickOnEvent();
		unRegisterBeforeFindByEvent();
		unRegisterBeforeNavigateBackEvent();
		unRegisterBeforeNavigateForwardEvent();
		unRegisterBeforeNavigateToEvent();
		unRegisterBeforeScriptEvent();

		unRegisterAfterChangeValueOfEvent();
		unRegisterAfterClickOnEvent();
		unRegisterAfterFindByEvent();
		unRegisterAfterNavigateBackEvent();
		unRegisterAfterNavigateForwardEvent();
		unRegisterAfterNavigateToEvent();
		unRegisterAfterScriptEvent();

		unRegisterOnExceptionEvent();
		return this;
	}

	public EventFiringWebApp registerAfterChangeValueOfEvent() {
		if (webDriverEventListenerAfterChangeValueOf == null) {
			throw new FrameworkException("registerAfterChangeValueOfEvent", "", "UNABLE TO REGISTER 'afterChangeValueOf' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterChangeValueOf);
		return this;
	}

	public EventFiringWebApp registerAfterClickOnEvent() {
		if (webDriverEventListenerAfterClickOn == null) {
			throw new FrameworkException("registerAfterClickOnEvent", "", "UNABLE TO REGISTER 'afterClickOn' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterClickOn);
		return this;
	}

	public EventFiringWebApp registerAfterFindByEvent() {
		if (webDriverEventListenerAfterFindBy == null) {
			throw new FrameworkException("registerAfterFindByEvent", "", "UNABLE TO REGISTER 'afterFindBy' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterFindBy);
		return this;
	}

	public EventFiringWebApp registerAfterNavigateBackEvent() {
		if (webDriverEventListenerAfterNavigateBack == null) {
			throw new FrameworkException("registerAfterNavigateBackEvent", "", "UNABLE TO REGISTER 'afterNavigateBack' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterNavigateBack);
		return this;
	}

	public EventFiringWebApp registerAfterNavigateForwardEvent() {
		if (webDriverEventListenerAfterNavigateForward == null) {
			throw new FrameworkException("registerAfterNavigateForwardEvent", "", "UNABLE TO REGISTER 'afterNavigateForward' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterNavigateForward);
		return this;
	}

	public EventFiringWebApp registerAfterNavigateToEvent() {
		if (webDriverEventListenerAfterNavigateTo == null) {
			throw new FrameworkException("registerAfterNavigateToEvent", "", "UNABLE TO REGISTER 'afterNavigateTo' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterNavigateTo);
		return this;
	}

	public EventFiringWebApp registerAfterScriptEvent() {
		if (webDriverEventListenerAfterScript == null) {
			throw new FrameworkException("registerAfterScriptEvent", "", "UNABLE TO REGISTER 'afterScript' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerAfterScript);
		return this;
	}

	public EventFiringWebApp registerBeforeChangeOnValueEvent() {
		if (webDriverEventListenerBeforeChangeValueOf == null) {
			throw new FrameworkException("registerBeforeChangeOnValueEvent", "", "UNABLE TO REGISTER 'beforeChangeOnValue' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeChangeValueOf);
		return this;
	}

	public EventFiringWebApp registerBeforeClickOnEvent() {
		if (webDriverEventListenerBeforeClickOn == null) {
			throw new FrameworkException("registerBeforeClickOnEvent", "", "UNABLE TO REGISTER 'beforeClickOn' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeClickOn);
		return this;
	}

	public EventFiringWebApp registerBeforeFindByEvent() {
		if (webDriverEventListenerBeforeFindBy == null) {
			throw new FrameworkException("registerBeforeFindByEvent", "", "UNABLE TO REGISTER 'beforeFindBy' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeFindBy);
		return this;
	}

	public EventFiringWebApp registerBeforeNavigateBackEvent() {
		if (webDriverEventListenerBeforeNavigateBack == null) {
			throw new FrameworkException("registerBeforeNavigateBackEvent", "", "UNABLE TO REGISTER 'beforeNavigateBack' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeNavigateBack);
		return this;
	}

	public EventFiringWebApp registerBeforeNavigateForwardEvent() {
		if (webDriverEventListenerBeforeNavigateForward == null) {
			throw new FrameworkException("registerBeforeNavigateForwardEvent", "", "UNABLE TO REGISTER 'beforeNavigateForward' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeNavigateForward);
		return this;
	}

	public EventFiringWebApp registerBeforeNavigateToEvent() {
		if (webDriverEventListenerBeforeNavigateTo == null) {
			throw new FrameworkException("registerBeforeNavigateToEvent", "", "UNABLE TO REGISTER 'beforeNavigateTo' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeNavigateTo);
		return this;
	}

	public EventFiringWebApp registerBeforeScriptEvent() {
		if (webDriverEventListenerBeforeScript == null) {
			throw new FrameworkException("registerBeforeNavigateToEvent", "", "UNABLE TO REGISTER 'beforeScript' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerBeforeScript);
		return this;
	}

	public EventFiringWebApp registerOnExceptionEvent() {
		if (webDriverEventListenerOnException == null) {
			throw new FrameworkException("registerOnExceptionEvent", "", "UNABLE TO REGISTER 'OnException' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).register(webDriverEventListenerOnException);
		return this;
	}
	// --- END: REGISTER SECTION ------------------------------------------------------------------

	// --- START: UNREGISTER SECTION --------------------------------------------------------------
	public EventFiringWebApp unRegisterAfterChangeValueOfEvent() {
		if (webDriverEventListenerAfterChangeValueOf == null) {
			throw new FrameworkException("unRegisterAfterChangeValueOfEvent", "", "UNABLE TO UNREGISTER 'afterChangeValueOf' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterChangeValueOf);
		return this;
	}

	public EventFiringWebApp unRegisterAfterClickOnEvent() {
		if (webDriverEventListenerAfterClickOn == null) {
			throw new FrameworkException("unRegisterAfterClickOnEvent", "", "UNABLE TO UNREGISTER 'afterClickOn' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterClickOn);
		return this;
	}

	public EventFiringWebApp unRegisterAfterFindByEvent() {
		if (webDriverEventListenerAfterFindBy == null) {
			throw new FrameworkException("unRegisterAfterFindByEvent", "", "UNABLE TO UNREGISTER 'afterFindBy' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterFindBy);
		return this;
	}

	public EventFiringWebApp unRegisterAfterNavigateBackEvent() {
		if (webDriverEventListenerAfterNavigateBack == null) {
			throw new FrameworkException("unRegisterAfterNavigateBackEvent", "", "UNABLE TO UNREGISTER 'afterNavigateBack' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterNavigateBack);
		return this;
	}

	public EventFiringWebApp unRegisterAfterNavigateForwardEvent() {
		if (webDriverEventListenerAfterNavigateForward == null) {
			throw new FrameworkException("unRegisterAfterNavigateForwardEvent", "", "UNABLE TO UNREGISTER 'afterNavigateForward' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterNavigateForward);
		return this;
	}

	public EventFiringWebApp unRegisterAfterNavigateToEvent() {
		if (webDriverEventListenerAfterNavigateTo == null) {
			throw new FrameworkException("unRegisterAfterNavigateToEvent", "", "UNABLE TO UNREGISTER 'afterNavigateTo' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterNavigateTo);
		return this;
	}

	public EventFiringWebApp unRegisterAfterScriptEvent() {
		if (webDriverEventListenerAfterScript == null) {
			throw new FrameworkException("unRegisterAfterScriptEvent", "", "UNABLE TO UNREGISTER 'afterScript' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerAfterScript);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeChangeOnValueEvent() {
		if (webDriverEventListenerBeforeChangeValueOf == null) {
			throw new FrameworkException("unRegisterBeforeChangeOnValueEvent", "", "UNABLE TO UNREGISTER 'beforeChangeOnValue' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeChangeValueOf);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeClickOnEvent() {
		if (webDriverEventListenerBeforeClickOn == null) {
			throw new FrameworkException("unRegisterBeforeClickOnEvent", "", "UNABLE TO UNREGISTER 'beforeClickOn' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeClickOn);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeFindByEvent() {
		if (webDriverEventListenerBeforeFindBy == null) {
			throw new FrameworkException("unRegisterBeforeFindByEvent", "", "UNABLE TO UNREGISTER 'beforeFindBy' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeFindBy);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeNavigateBackEvent() {
		if (webDriverEventListenerBeforeNavigateBack == null) {
			throw new FrameworkException("unRegisterBeforeNavigateBackEvent", "", "UNABLE TO UNREGISTER 'beforeNavigateBack' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeNavigateBack);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeNavigateForwardEvent() {
		if (webDriverEventListenerBeforeNavigateForward == null) {
			throw new FrameworkException("unRegisterBeforeNavigateForwardEvent", "", "UNABLE TO UNREGISTER 'beforeNavigateForward' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeNavigateForward);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeNavigateToEvent() {
		if (webDriverEventListenerBeforeNavigateTo == null) {
			throw new FrameworkException("unRegisterBeforeNavigateToEvent", "", "UNABLE TO UNREGISTER 'beforeNavigateTo' EVENT", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeNavigateTo);
		return this;
	}

	public EventFiringWebApp unRegisterBeforeScriptEvent() {
		if (webDriverEventListenerBeforeScript == null) {
			throw new FrameworkException("unRegisterBeforeScriptEvent", "", "UNABLE TO UNREGISTER 'beforeScript' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerBeforeScript);
		return this;
	}

	public EventFiringWebApp unRegisterOnExceptionEvent() {
		if (webDriverEventListenerOnException == null) {
			throw new FrameworkException("unRegisterOnExceptionEvent", "", "UNABLE TO UNREGISTER 'OnException' EVENT", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		((EventFiringWebDriver) browser.getWebDriver()).unregister(webDriverEventListenerOnException);
		return this;
	}
	// --- END: UNREGISTER SECTION ----------------------------------------------------------------

	// --- END: PUBLIC METHOD SECTION -----------------------------------------------------------

	// --- START: PROTECTED METHOD SECTION --------------------------------------------------------
	protected void afterChangeValueOfEvent(WebElement element, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void afterClickOnEvent(WebElement element, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void afterFindByEvent(By by, WebElement element, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void afterNavigateBackEvent(WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void afterNavigateForwardEvent(WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void afterNavigateToEvent(String url, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void afterScriptEvent(String script, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeChangeValueOfEvent(WebElement element, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeClickOnEvent(WebElement element, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeFindByEvent(By by, WebElement element, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeNavigateBackEvent(WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeNavigateForwardEvent(WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeNavigateToEvent(String url, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void beforeScriptEvent(String script, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}

	protected void onExceptionEvent(Throwable throwable, WebDriver driver) {
		// SCRIPTER CODE TO HANDLE EVENT GOES HERE
	}
	// --- END: PROTECTED METHOD SECTION --------------------------------------------------------

	// --- START: PRIVATE METHOD SECTION ----------------------------------------------------------
	private void init() {
		// Tell browser to use "EventFiringWebDriver" instead of "WebDriver".
		browser.setWebDriver(new EventFiringWebDriver(browser.getWebDriver()), "EventFiringWebApp");

		// Setup event listeners
		webDriverEventListenerAfterChangeValueOf = new AbstractWebDriverEventListener() {
			@Override
			public void afterChangeValueOf(WebElement element, WebDriver driver) {
				afterChangeValueOfEvent(element, driver);
			}
		};

		webDriverEventListenerAfterClickOn = new AbstractWebDriverEventListener() {
			@Override
			public void afterClickOn(WebElement element, WebDriver driver) {
				afterClickOnEvent(element, driver);
			}

		};

		webDriverEventListenerAfterFindBy = new AbstractWebDriverEventListener() {
			@Override
			public void afterFindBy(By by, WebElement element, WebDriver driver) {
				afterFindByEvent(by, element, driver);
			}
		};

		webDriverEventListenerAfterNavigateBack = new AbstractWebDriverEventListener() {
			@Override
			public void afterNavigateBack(WebDriver driver) {
				afterNavigateBackEvent(driver);
			}
		};

		webDriverEventListenerAfterNavigateForward = new AbstractWebDriverEventListener() {
			@Override
			public void afterNavigateForward(WebDriver driver) {
				afterNavigateForwardEvent(driver);
			}
		};

		webDriverEventListenerAfterNavigateTo = new AbstractWebDriverEventListener() {
			@Override
			public void afterNavigateTo(String url, WebDriver driver) {
				afterNavigateToEvent(url, driver);
			}
		};

		webDriverEventListenerAfterScript = new AbstractWebDriverEventListener() {
			@Override
			public void afterScript(String script, WebDriver driver) {
				afterScriptEvent(script, driver);
			}
		};

		webDriverEventListenerBeforeChangeValueOf = new AbstractWebDriverEventListener() {
			@Override
			public void beforeChangeValueOf(WebElement element, WebDriver driver) {
				beforeChangeValueOfEvent(element, driver);
			}
		};

		webDriverEventListenerBeforeClickOn = new AbstractWebDriverEventListener() {
			@Override
			public void beforeClickOn(WebElement element, WebDriver driver) {
				beforeClickOnEvent(element, driver);
			}
		};

		webDriverEventListenerBeforeFindBy = new AbstractWebDriverEventListener() {
			@Override
			public void beforeFindBy(By by, WebElement element, WebDriver driver) {
				beforeFindByEvent(by, element, driver);
			}
		};

		webDriverEventListenerBeforeNavigateBack = new AbstractWebDriverEventListener() {
			@Override
			public void beforeNavigateBack(WebDriver driver) {
				beforeNavigateBackEvent(driver);
			}
		};

		webDriverEventListenerBeforeNavigateForward = new AbstractWebDriverEventListener() {
			@Override
			public void beforeNavigateForward(WebDriver driver) {
				beforeNavigateForwardEvent(driver);
			}
		};

		webDriverEventListenerBeforeNavigateTo = new AbstractWebDriverEventListener() {
			@Override
			public void beforeNavigateTo(String url, WebDriver driver) {
				beforeNavigateToEvent(url, driver);
			}
		};

		webDriverEventListenerBeforeScript = new AbstractWebDriverEventListener() {
			@Override
			public void beforeScript(String script, WebDriver driver) {
				beforeScriptEvent(script, driver);
			}
		};

		webDriverEventListenerOnException = new AbstractWebDriverEventListener() {
			@Override
			public void onException(Throwable throwable, WebDriver driver) {
				onExceptionEvent(throwable, driver);
			}
		};
	}
	// --- END: PRIVATE METHOD SECTION ----------------------------------------------------------
}