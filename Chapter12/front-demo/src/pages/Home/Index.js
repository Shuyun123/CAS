import React, { Component } from 'react';
import { connect } from 'dva';
import { Card, Row, Col, Divider } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import styles from './Index.less';

@connect(({ user, login }) => ({
  user, login
}))
class Index extends Component {
  state = {
  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'user/authinfo',
    });
  }

  logout = () => {
    window.location.href="https://sso.anumbrella.net:8443/cas/logout?service=http://client.anumbrella.net:8080/api/user/caslogout"
    // const { user: { currentUser } } = this.props;
    // const { dispatch } = this.props;
    // dispatch({
    //   type: 'login/logout',
    //   payload: { ...currentUser }
    // });
  }

  render() {

    const { user: { currentUser } } = this.props;

    return (
      <div className={styles.wrapper}>
        <GridContent>
          <Row gutter={24}>
            <Col lg={9} md={24}>
              <Card bordered={false} style={{ marginTop: 24, marginBottom: 24, border: '1px solid #3a2e2ea6' }}>
                <div className={styles.avatar}>
                  <img alt="" src="https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png" />
                </div>
                <Divider dashed />
                <div className={styles.detail}>
                  <span>姓名：{currentUser.username}</span>
                  <span>账号：{currentUser.id}</span>
                  <span>电话：{currentUser.phone}</span>
                  <span>邮箱：{currentUser.email}</span>
                </div>
                <Divider dashed />
                <a className={styles.modify_pass} href="#" onClick={this.logout}>退出登录</a>
              </Card>
            </Col>
          </Row>
        </GridContent>
      </div>
    );
  }
}

export default Index;
