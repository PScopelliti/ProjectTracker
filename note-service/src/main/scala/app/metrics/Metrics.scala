package app.metrics

trait Metrics {
  /**
   * Entry point to metrics/stats collection for the system.
   *
   * Basic stats receiver, use `StatsReceiver.scope` to customise. Use this for creating stats instances for things
   * like downstream services:
   *
   * {{{
   * Metrics.baseMetrics.scope("clnt", "my-client").counter("requests").incr()
   * }}}
   *
   * For more information on how to use these metrics, and which kind of things to use for what, see:
   * http://twitter.github.io/util/guide/util-stats/index.html
   *
   * For details on what is monitored automagically by Finagle, see https://twitter.github.io/finagle/guide/Metrics.html
   *
   * Note that we back stats with [[http://metrics.dropwizard.io/3.1.0/ Dropwizard Metrics]], which has some particular
   * requirements about names, esp. it doesn't like name clashes.
   */
  final lazy val baseMetrics: AppStatsReceiver = AppStatsReceiver.stats

  /**
   * A stats receiver to use when sending metrics about downstream services. Stats are prefixed with 'clnt' and the
   * name of the system. Do not use this for server metrics, use [[clientMetrics]] instead.
   */
  final lazy val clientMetrics: AppStatsReceiver = baseMetrics.scope("client")

  /**
   * A stats receiver to use when sending metrics about the service itself. Stats are prefixed with 'srv' and the
   * name of the system. Do not use this for downstream client metrics, use [[serverMetrics]] instead.
   */
  final lazy val serverMetrics: AppStatsReceiver = baseMetrics.scope("server")
}

object Metrics extends Metrics

