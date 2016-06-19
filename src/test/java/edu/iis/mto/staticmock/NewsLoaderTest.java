package edu.iis.mto.staticmock;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
	
	@Mock
	private ConfigurationLoader configurationLoader;
	private NewsReader newsReader;
	private NewsLoader newsLoader;
	private IncomingNews incomingNews;
	private PublishableNews publishableNews;
	
	@Before
	public void init() {
		mockStatic(ConfigurationLoader.class);
		mockStatic(NewsReader.class);
		mockStatic(NewsReaderFactory.class);
		
		configurationLoader = mock(ConfigurationLoader.class);
		newsReader = mock(NewsReader.class);
		
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(configurationLoader.loadConfiguration()).thenReturn(new Configuration());
		when(NewsReaderFactory.getReader(Mockito.anyString())).thenReturn(newsReader);
		when(newsReader.read()).thenReturn(new IncomingNews());
		
		newsLoader = new NewsLoader();
		incomingNews = new IncomingNews();
		
		incomingNews.add(new IncomingInfo("a", SubsciptionType.A));
		incomingNews.add(new IncomingInfo("a", SubsciptionType.B));
		incomingNews.add(new IncomingInfo("a", SubsciptionType.C));
		incomingNews.add(new IncomingInfo("a", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("a", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("a", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("a", SubsciptionType.NONE));
		
		when(newsReader.read()).thenReturn(incomingNews);
	}	

	@Test
	public void testForPublicNews() {
		NewsLoader newsLoader = new NewsLoader();
		PublishableNews publishableNews = newsLoader.loadNews();
		
		System.out.println(publishableNews.getSubscribentContent().size());
		assertThat(publishableNews.getPublicContent().size(), equalTo(4));
		assertThat(publishableNews.getSubscribentContent().size(), equalTo(3));
	}
	

	@Test
	public void testForBehavior() {
		publishableNews = newsLoader.loadNews();
		verify(configurationLoader, times(1)).loadConfiguration();
		verify(newsReader, times(1)).read();
	}
	

}