//package com.rexel.mqtt.old;
//
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.springframework.core.log.LogAccessor;
//import org.springframework.lang.Nullable;
//import org.springframework.messaging.MessagingException;
//import org.springframework.util.Assert;
//
//import java.util.LinkedHashSet;
//import java.util.Set;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * @ClassName MqttPaho
// * @Description mqtt客户端 发布订阅
// * @Author 孟开通
// * @Date 2022/8/29 14:19
// **/
//public abstract class AbstractMqttPahoAdapter {
//	protected final LogAccessor logger = new LogAccessor(getClass());
//	protected final Lock topicLock = new ReentrantLock();
//	protected final ReentrantLock lifecycleLock = new ReentrantLock();
//	/**
//	 * mqtt url
//	 */
//	private final String url;
//	/**
//	 * mqtt客户端id
//	 */
//	private final String clientId;
//	/**
//	 * topic
//	 */
//	private final Set<Topic> topics = new LinkedHashSet<>();
//	private volatile boolean running;
//	private volatile boolean active;
//
//	public AbstractMqttPahoAdapter(@Nullable String url, String clientId) {
//		Assert.hasText(clientId, "'clientId' cannot be null or empty");
//		this.url = url;
//		this.clientId = clientId;
//	}
//
//	public int[] getQos() {
//		this.topicLock.lock();
//		try {
//			int[] topicQos = new int[this.topics.size()];
//			int n = 0;
//			for (Topic topic : this.topics) {
//				topicQos[n++] = topic.getQos();
//			}
//			return topicQos;
//		} finally {
//			this.topicLock.unlock();
//		}
//	}
//
//	public void setQos(int... qos) {
//		Assert.notNull(qos, "'qos' cannot be null");
//		if (qos.length == 1) {
//			for (Topic topic : this.topics) {
//				topic.setQos(qos[0]);
//			}
//		} else {
//			Assert.isTrue(qos.length == this.topics.size(),
//					"When setting qos, the array must be the same length as the topics");
//			int n = 0;
//			for (Topic topic : this.topics) {
//				topic.setQos(qos[n++]);
//			}
//		}
//	}
//
//	@Nullable
//	protected String getUrl() {
//		return this.url;
//	}
//
//	protected String getClientId() {
//		return this.clientId;
//	}
//
//
//	public String[] getTopic() {
//		this.topicLock.lock();
//		try {
//			String[] topicNames = new String[this.topics.size()];
//			int n = 0;
//			for (Topic topic : this.topics) {
//				topicNames[n++] = topic.getTopic();
//			}
//			return topicNames;
//		} finally {
//			this.topicLock.unlock();
//		}
//	}
//
//	protected void addTopic(String topic, int qos) {
//		this.topicLock.lock();
//		try {
//			Topic topik = new Topic(topic, qos);
//			if (this.topics.contains(topik)) {
//				return;
//			}
//			this.topics.add(topik);
//			if (this.logger.isDebugEnabled()) {
//				logger.debug("Added '" + topic + "' to subscriptions.");
//			}
//		} finally {
//			this.topicLock.unlock();
//		}
//	}
//
//	public void addTopic(String... topic) {
//		Assert.notNull(topic, "'topic' cannot be null");
//		this.topicLock.lock();
//		try {
//			for (String t : topic) {
//				addTopic(t, 1);
//			}
//		} finally {
//			this.topicLock.unlock();
//		}
//	}
//
//	public void addTopics(String[] topic, int[] qos) {
//		Assert.notNull(topic, "'topic' cannot be null.");
//		Assert.noNullElements(topic, "'topic' cannot contain any null elements.");
//		Assert.isTrue(topic.length == qos.length, "topic and qos arrays must the be the same length.");
//		this.topicLock.lock();
//		try {
//			for (String topik : topic) {
//				if (this.topics.contains(new Topic(topik, 0))) {
//					throw new MessagingException("Topic '" + topik + "' is already subscribed.");
//				}
//			}
//			for (int i = 0; i < topic.length; i++) {
//				addTopic(topic[i], qos[i]);
//			}
//		} finally {
//			this.topicLock.unlock();
//		}
//	}
//
//	public void removeTopic(String... topic) {
//		this.topicLock.lock();
//		try {
//			for (String t : topic) {
//				if (this.topics.remove(new Topic(t, 0)) && this.logger.isDebugEnabled()) {
//					logger.debug("Removed '" + t + "' from subscriptions.");
//				}
//			}
//		} finally {
//			this.topicLock.unlock();
//		}
//	}
//
//	public boolean isActive() {
//		return this.active;
//	}
//
//	public final void start() throws MqttException {
//		this.lifecycleLock.lock();
//		try {
//			if (!this.running) {
//				this.active = true;
//				doStart();
//				this.running = true;
//				if (logger.isInfoEnabled()) {
//					logger.info("started " + this);
//				}
//			}
//		} finally {
//			this.lifecycleLock.unlock();
//		}
//	}
//
//	public final boolean isRunning() {
//		return this.running;
//	}
//
//	public final void stop() {
//		this.lifecycleLock.lock();
//		try {
//			if (this.running) {
//				this.active = false;
//				doStop();
//				this.running = false;
//				if (logger.isInfoEnabled()) {
//					logger.info("stopped " + this);
//				}
//			}
//		} finally {
//			this.lifecycleLock.unlock();
//		}
//	}
//
//
//	protected abstract void doStart() throws MqttException;
//
//	protected abstract void doStop();
//
//
//	/**
//	 * @since 4.1
//	 */
//	private static final class Topic {
//
//		private final String topic;
//
//		private volatile int qos;
//
//		Topic(String topic, int qos) {
//			this.topic = topic;
//			this.qos = qos;
//		}
//
//		private int getQos() {
//			return this.qos;
//		}
//
//		private void setQos(int qos) {
//			this.qos = qos;
//		}
//
//		private String getTopic() {
//			return this.topic;
//		}
//
//		@Override
//		public int hashCode() {
//			return this.topic.hashCode();
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj) {
//				return true;
//			}
//			if (obj == null) {
//				return false;
//			}
//			if (getClass() != obj.getClass()) {
//				return false;
//			}
//			Topic other = (Topic) obj;
//			if (this.topic == null) {
//				return other.topic == null;
//			} else return this.topic.equals(other.topic);
//		}
//
//		@Override
//		public String toString() {
//			return "Topic [topic=" + this.topic + ", qos=" + this.qos + "]";
//		}
//
//	}
//
//}
